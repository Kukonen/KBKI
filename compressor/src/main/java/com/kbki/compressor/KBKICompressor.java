package com.kbki.compressor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KBKICompressor {
    // src - source file, dst - new compressed file
    // filename - name of new file
    public static void LZW(String src, String dst) {

        try(
                BufferedReader reader = new BufferedReader(new FileReader(src));
                BufferedWriter writer = new BufferedWriter(new FileWriter(dst))
        ) {
            // filled dictionary, base use 8 bit length
            int dictionaryLength = 256;
            Map<String, Integer> dictionary = new HashMap<>();
            // append to dictionary codes of symbols
            for (int i = 0; i < dictionaryLength; i++) {
                dictionary.put(String.valueOf((char) i), i);
            }

            // byte, which we remember after step
            String rememberByte = "";

            // read all bytes from source file step by step
            while(reader.ready()) {
                // get new byte from source file
                char newByte = (char) reader.read();
                // two bytes, which we use in algorithm
                String pairBytes = rememberByte + newByte;
                if (dictionary.containsKey(pairBytes)) {
                    rememberByte = pairBytes;
                } else {
                    writer.write(dictionary.get(rememberByte));
                    dictionary.put(pairBytes, dictionaryLength++);
                    rememberByte = String.valueOf(newByte);
                }
            }
            // add last charter to file
            if (!rememberByte.isEmpty()) {
                writer.write(dictionary.get(rememberByte));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void LZWdecode(String src, String dst) {

        try(
                BufferedReader reader = new BufferedReader(new FileReader(src));
                BufferedWriter writer = new BufferedWriter(new FileWriter(dst))
        ) {
            // filled dictionary, base use 8 bit length
            int dictionaryLength = 256;
            Map<Integer, String> dictionary = new HashMap<>();
            // append to dictionary codes of symbols
            for (int i = 0; i < dictionaryLength; i++) {
                dictionary.put(i, String.valueOf((char) i));
            }

            // bytes that's we read from file
            int readablyBytes = reader.read();
            // sequence of bytes
            String bytes = String.valueOf((char) readablyBytes);
            writer.write(dictionary.get(readablyBytes));

            while (reader.ready()) {
                readablyBytes = reader.read();
                // bites, that's we write in file, when find relationship
                // from dictionary
                String writableBytes = dictionary.containsKey(readablyBytes)
                        ? dictionary.get(readablyBytes)
                        : bytes + bytes.charAt(0);

                // write bytes in file
                writer.write(writableBytes);

                // add in dictionary new combinations
                dictionary.put(dictionaryLength++, bytes + writableBytes.charAt(0));
                bytes = writableBytes;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
