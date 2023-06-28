package org.kbki;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        KBKIFilesGenerator kbkiFilesGenerator = new KBKIFilesGenerator();
        try {
            kbkiFilesGenerator.generateDefaultRGBNoCompression("C:/Users/evgen/OneDrive/Рабочий стол/projects/KBKI/origin.kbki");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}