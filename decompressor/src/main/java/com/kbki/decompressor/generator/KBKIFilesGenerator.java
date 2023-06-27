package com.kbki.decompressor.generator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

public class KBKIFilesGenerator {
    Random random;

    int numberOfComponents;

    public KBKIFilesGenerator() {
        random = new Random();
        numberOfComponents = 3;
    }

    public void generateDefaultRGBNoCompression(String filename) throws IOException {
        FileOutputStream file = new FileOutputStream(filename, true);
        generateRandom(file, (byte) 0, (byte) 0, (byte) 0);
    }

    public void generateRandom(FileOutputStream file, byte filter, byte colorType, byte compressionType) throws IOException {
        int height = 256;
        int width = 256;

        file.write(0);
        file.write(ByteBuffer.allocate(4).putInt(height).array());
        file.write(ByteBuffer.allocate(4).putInt(width).array());
        file.write(filter);
        file.write(colorType);
        file.write(compressionType);
        file.write(0);

        for (int y = 0; y < height; y++) {
            byte lineFilter = 0;
            file.write(lineFilter);

            byte[] linePixels = new byte[width * numberOfComponents];

            random.nextBytes(linePixels);
            file.write(linePixels);
        }
    }
}
