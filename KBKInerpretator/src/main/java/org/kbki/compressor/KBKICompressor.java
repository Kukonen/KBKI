package org.kbki.compressor;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.kbki.utils.KBKI;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class KBKICompressor {
    private KBKI kbki = new KBKI();

    private String sourceFileName;
    private String resultFileName;

    private byte mode;

    public KBKICompressor setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
        return this;
    }

    public KBKICompressor setResultFileName(String resultFileName) {
        this.resultFileName = resultFileName;
        return this;
    }

    public KBKICompressor readMetadata() throws IOException{
        FileInputStream fileInputStream = new FileInputStream(sourceFileName);
        byte[] buffer = fileInputStream.readNBytes(13);
        byte size = 0;
        byte[] key = new byte[0];

        // if encryption has, then read key
        int encryptionType = (int)buffer[12];

        if (encryptionType != 0) {
            size = fileInputStream.readNBytes(1)[0];

            // if encryption has key with length > 0
            if (size > 0) {
                key = fileInputStream.readNBytes((int) size);
            }
        }

        kbki.setMetadata(buffer, size, key);

        return this;
    }

    private long getSkipLength() {
        long skip = 13;

        if (kbki.getEncryptionType() != 0) {
            skip += 1;
            skip += kbki.getEncryptionKeyLength();
        }

        return skip;
    }
    public KBKICompressor compress() {

        // TODO create exception
        // if compression is already exist, we don't use compression
        if(kbki.getCompressionType() != 0) {
            return this;
        }

        switch (mode) {
            case 0 -> {
                break;
            }
            case (byte) 129 -> {
                // Deflate
                LZWCompress();
                break;
            }
            case (byte) 130 -> {
                // Deflate
                deflateCompress();
                break;
            }
        }

        return this;
    }

    public KBKICompressor deflate() {
        mode = (byte) 130;
        return this;
    }

    public KBKICompressor LZW() {
        mode = (byte) 129;
        return this;
    }

    private void deflateCompress() {
        try(
                FileInputStream inputStream = new FileInputStream(sourceFileName);
                FileOutputStream outputStream = new FileOutputStream(resultFileName);
                DeflateCompressorOutputStream compressor = new DeflateCompressorOutputStream(outputStream)
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            bytesRead = inputStream.readNBytes(buffer, 0, (int) this.getSkipLength());
            buffer[11] = (byte) 130;
            outputStream.write(buffer, 0, bytesRead);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                compressor.write(buffer, 0, bytesRead);
            }

            compressor.finish();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // src - source file, dst - new compressed file
    // filename - name of new file
    private void LZWCompress() {
        try(
                BufferedReader reader = new BufferedReader(new FileReader(sourceFileName));
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName))
        ) {
            char[] buffer = new char[1024];

            reader.read(buffer, 0, (int) this.getSkipLength());
            buffer[11] = (char) ((byte) 129);
            writer.write(buffer);

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
}