import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Stream;

public class KBKIPrinter {

    private int imageHeight;
    private int imageWidth;

    /** Представляет собой массив байтов, каждые n из которых образуют один пиксель, где n - число цветовых компонент */
    private byte[] pixels;

    private ColorModel colorModel;
    private int numberOfComponents;


    public void showImageFromDecompressedFile(String sourceFileName) throws InvalidFileStructureException {
        // decompress file and get necessary info
        try (FileInputStream file = new FileInputStream(sourceFileName)) {
            readMetaInfo(file);
            readColors(file);
            //saveToImage(getBufferedImage(), "test.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showImage(BufferedImage image) throws IOException {
        new FileOutput(image);
    }

    private void saveToImage(BufferedImage image, String resultFileName) throws IOException {
        ImageIO.write(image, "png", new File(resultFileName));
    }

    private BufferedImage getBufferedImage() {
        DataBuffer buffer = new DataBufferByte(pixels, imageHeight * imageWidth);

        // Хз для чего нужно, но нужно(
        int[] bandOffsets = new int[numberOfComponents];
        Arrays.setAll(bandOffsets, i -> i++);

        //3 bytes per pixel: red, green, blue
        WritableRaster raster = Raster.createInterleavedRaster(buffer, imageWidth, imageHeight, numberOfComponents * imageWidth, numberOfComponents, bandOffsets, null);
        ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, colorModel.isAlphaPremultiplied(), Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

        return new BufferedImage(cm, raster, true, null);
    }

    private void readMetaInfo(FileInputStream fileInputStream) throws InvalidFileStructureException, IOException {
        byte[] metaInfo = fileInputStream.readNBytes(FileConfig.META_INFO_LENGTH);

        imageHeight = ByteBuffer.wrap(Arrays.copyOfRange(metaInfo, FileConfig.HEIGHT_BYTES_START, FileConfig.HEIGHT_BYTES_START + 4)).getInt();
        imageWidth = ByteBuffer.wrap(Arrays.copyOfRange(metaInfo, FileConfig.WIDTH_BYTES_START, FileConfig.WIDTH_BYTES_START + 4)).getInt();

        if (imageHeight == 0 && imageWidth == 0) {
            throw new InvalidFileStructureException("Invalid size of image in file");
        }

        byte colorType = metaInfo[FileConfig.COLOR_TYPE_START];

        // Getting color type:
        switch (colorType) {
            case 0 -> {
                colorModel = ColorModel.getRGBdefault();
                numberOfComponents = 3;
            }
            case 1 -> // хз как
                    numberOfComponents = 4;
            default -> throw new InvalidFileStructureException("Invalid color type in file");
        }
    }

    private void readColors(FileInputStream fileInputStream) throws IOException {
        // Initialize pixels array
        pixels = new byte[imageWidth * numberOfComponents * imageHeight];

        // and now I start to read colors (pixels):
        for (int y = 0; y < imageHeight; y++) {
            //byte[] linePixels = fileInputStream.readNBytes(imageWidth * 3 + 1);

            ByteBuffer byteBuffer = ByteBuffer.wrap(fileInputStream.readNBytes(imageWidth * 3 + 1));
            System.out.println(Arrays.toString(byteBuffer.array()));

            //// byte lineFilter = linePixels[0];
            //for (int i = 1; i < imageWidth * 3 + 1; i++) {
            //    pixels[y * imageWidth * 3 + i - 1] = linePixels[i];
            //}
        }
    }
}
