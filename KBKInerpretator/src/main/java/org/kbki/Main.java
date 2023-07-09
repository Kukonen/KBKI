package org.kbki;

import org.kbki.compressor.KBKICompressor;
import org.kbki.decompressor.KBKIDecompressor;
import org.kbki.generator.KBKIFilesGenerator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        KBKIDecompressor decompressor = new KBKIDecompressor();
        KBKICompressor compressor = new KBKICompressor();
        try {
            compressor
                    .setSourceFileName(origin)
                    .setResultFileName(compress)
                    .readMetadata()
                    .deflate()
                    .compress();

            decompressor
                    .setSourceFileName(compress)
                    .setResultFileName(decompress)
                    .addWrite()
                    .readMetadata()
                    .decompress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}