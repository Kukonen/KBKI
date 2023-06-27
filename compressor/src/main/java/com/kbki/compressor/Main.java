package com.kbki.compressor;

import java.io.*;
import java.util.zip.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\evgen\\OneDrive\\Рабочий стол\\projects\\KBKI\\Билеты по ЭВМ [forked].pdf";
        String compressedFilePath = "C:\\Users\\evgen\\OneDrive\\Рабочий стол\\projects\\KBKI\\Билеты по ЭВМ [forked] comp.zip";
        String outputFilePath = "C:\\Users\\evgen\\OneDrive\\Рабочий стол\\projects\\KBKI\\Билеты по ЭВМ [forked] 2.pdf";

        // Сжатие файла
        try {
            FileInputStream fis = new FileInputStream(inputFilePath);
            FileOutputStream fos = new FileOutputStream(compressedFilePath);
            DeflaterOutputStream dos = new DeflaterOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, len);
            }
            fis.close();
            dos.finish();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Разжатие файла
        try {
            FileInputStream fis = new FileInputStream(compressedFilePath);
            InflaterInputStream iis = new InflaterInputStream(fis);
            FileOutputStream fos = new FileOutputStream(outputFilePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = iis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            iis.close();
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}