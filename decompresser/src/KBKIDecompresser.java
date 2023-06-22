import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

enum ColorTypes {
    RGB,
    RGBA
}

public class KBKIDecompresser implements FileDecompresser {
    private int imageHeight;
    private int imageWidth;

    /** Представляет собой массив байтов, каждые n из которых образуют один пиксель, где n - число цветовых компонент */
    private byte[] pixels;

    ColorModel colorModel;
    int numberOfComponents;

    @Override
    public void decompressToFile(String sourceFileName, String resultFileName) {
        // I'm decompress your sourceFile and write result to resultFile
    }

    @Override
    public void decompressAndShowImage(String sourceFileName) throws DecompressingFailedException {
        // decompress file and get necessary info
        decompress();
        try {
            saveToImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToImage() throws IOException {
        DataBuffer buffer = new DataBufferByte(pixels, imageHeight * imageWidth);

        // Хз для чего нужно, но нужно(
        int[] bandOffsets = new int[numberOfComponents];
        Arrays.setAll(bandOffsets, i -> i++);

        //3 bytes per pixel: red, green, blue
        WritableRaster raster = Raster.createInterleavedRaster(buffer, imageWidth, imageHeight, numberOfComponents * imageWidth, numberOfComponents, bandOffsets, null);
        ColorModel cm = new ComponentColorModel(colorModel.getColorSpace(), false, colorModel.isAlphaPremultiplied(), Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(cm, raster, true, null);

        ImageIO.write(image, "png", new File("image.png"));
    }

    @Override
    public ArrayList<String> decompress() throws DecompressingFailedException {
        // here i'm decompressing file and got meta info for example:
        String height = "0000000000000000001111111100";
        String width = "0000000000000000011110111100";
        String filter = "00000000";
        String colorType = "00000000";
        String mask = "00000000";
        String compressionType = "00000000";

        // Getting color type:
        switch (Integer.parseInt(colorType)) {
            case 0 -> {
                colorModel = ColorModel.getRGBdefault();
                numberOfComponents = 3;
            }
            case 1 -> {
                // хз как
                numberOfComponents = 4;
            }
            default -> {
                throw new DecompressingFailedException("Invalid color type in file");
            }
        }

        // push all meta info to ArrayList
        ArrayList<String> decompressedResult = Stream.of(height, width).collect(Collectors.toCollection(ArrayList::new));

        // and now I start to read and decompress colors (pixels):
        imageHeight = Integer.parseInt(height, 2);
        imageWidth = Integer.parseInt(width, 2);

        if (imageHeight == 0 && imageWidth == 0) {
            System.err.println();
            throw new DecompressingFailedException("Invalid size of image in file");
        }

        // Initialize pixels array
        pixels = new byte[imageWidth * numberOfComponents * imageHeight];

        for (int y = 0; y < imageHeight; y++) {
            String lineFilter;
            for (int x = 0; x < imageWidth * 3 + 1; x++) {
                if (x % (imageWidth * 3 + 1) == 0) {
                    // but from file
                    lineFilter = "00000000";
                    decompressedResult.add(lineFilter);
                    continue;
                }
                // here I read and decompress current pixel
                Random random = new Random();
                byte colorComponent = ((byte) (random.nextInt(0, 256)));

                // but from decompressed info
                decompressedResult.add("00000000");

                pixels[x - 1 + y * (imageWidth * 3)] = colorComponent;
            }
        }

        return decompressedResult;
    }
}
