import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KBKIDecompresser implements FileDecompresser {
    private int imageHeight;
    private int imageWidth;

    private int PIXELS_START = 6;

    private ArrayList<String> pixels;

    @Override
    public void decompressToFile(String sourceFileName, String resultFileName) {
        // I'm decompress your sourceFile and write result to resultFile
    }

    @Override
    public void decompressAndShowImage(String sourceFileName) {
        decompress();
        startPyScript();
    }

    private void startPyScript() {
        
    }

    @Override
    public ArrayList<String> decompress() {
        pixels = new ArrayList<>();

        // here i'm decompressing file and got meta info for example:
        String height = "00000000000000000000000000000010";
        String width = "00000000000000000000000000000010";
        String filter = "00000000";
        String colorType = "00000000";
        String mask = "00000000";
        String compressionType = "00000000";

        ArrayList<String> decompressedResult = Stream.of(height, width).collect(Collectors.toCollection(ArrayList::new));

        // and now I start to read and decompress colors (pixels):
        imageHeight = Integer.parseInt(height, 2);
        imageWidth = Integer.parseInt(width, 2);

        for (int i = 0; i < imageHeight; i++) {
            String lineFilter;
            for (int j = 0; j < imageWidth + 1; j++) {
                if (j % (imageWidth + 1) == 0) {
                    // but from file
                    lineFilter = "00000000";
                    decompressedResult.add(lineFilter);
                    continue;
                }
                // here I read and decompress current pixel
                Random random = new Random();

                String pixel = "#" + (random.nextInt(100000) + 899999);

                decompressedResult.add(pixel);
                pixels.add(pixel);
            }
        }

        return decompressedResult;
    }
}
