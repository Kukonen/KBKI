package org.kbki;

import org.kbki.compressor.KBKICompressor;
import org.kbki.decompressor.KBKIDecompressor;
import org.kbki.generator.KBKIFilesGenerator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        KBKIFilesGenerator kbkiFilesGenerator = new KBKIFilesGenerator();
//        try {
//            kbkiFilesGenerator.generateDefaultRGBNoCompression("C:/Users/evgen/OneDrive/Рабочий стол/projects/KBKI/origin.kbki");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        String origin = "C:/Users/evgen/OneDrive/Рабочий стол/projects/KBKI/Билеты по ЭВМ [forked].pdf";
        String compress = "C:/Users/evgen/OneDrive/Рабочий стол/projects/KBKI/Билеты по ЭВМ [forked] in.pdf";
        String decompress = "C:/Users/evgen/OneDrive/Рабочий стол/projects/KBKI/Билеты по ЭВМ [forked] out.pdf";

        KBKIDecompressor decompressor = new KBKIDecompressor();
        KBKICompressor compressor = new KBKICompressor();
        try {
            compressor
                    .setSourceFileName(origin)
                    .setResultFileName(compress)
                    .readMetadata()
                    .compress()
                    .deflateCompress();

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