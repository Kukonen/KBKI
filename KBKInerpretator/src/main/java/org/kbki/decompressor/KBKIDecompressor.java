package org.kbki.decompressor;

import org.kbki.utils.KBKI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class KBKIDecompressor {

    private KBKI kbki = new KBKI();
    private String sourceFileName;
    private String resultFileName;

    // modes for decoding executions
    private boolean show = false;
    private boolean write = false;

    public KBKIDecompressor setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
        return this;
    }

    public KBKIDecompressor setResultFileName(String resultFileName) {
        this.resultFileName = resultFileName;
        return this;
    }

    public KBKIDecompressor readMetadata() throws IOException{
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

    public KBKIDecompressor addShow() {
        this.show = true;
        return this;
    }

    public KBKIDecompressor addWrite() {
        this.write = true;
        return this;
    }

    public void decompress() {
        // if any mode hasn't set
        if (!(this.show || this.write)) {
            return;
        }

        switch (kbki.getCompressionType()) {
            case 0 -> {
                break;
            }
            case (byte) 130 -> {
                // Deflate
                deflateDecompress();
                break;
            }
        }
    }

    // count bytes of metadata
    private long getSkipLength() {
        long skip = 13;

        if (kbki.getEncryptionType() != 0) {
            skip += 1;
            skip += (int) kbki.getEncryptionKeyLength();
        }

        return skip;
    }

    private void deflateDecompress() {
        // TODO make divide input and output
        // TODO set compression type byte to 0
        try(
                FileInputStream reader = new FileInputStream(sourceFileName);
                FileOutputStream writer = new FileOutputStream(resultFileName);
                InflaterInputStream inflate = new InflaterInputStream(reader)
        ) {
//            byte[] buffer;
//            int len;
//
//            buffer = reader.readNBytes((int) this.getSkipLength());
//            writer.write(buffer);
//
//            buffer = new byte[1024];
//
//            while ((len = reader.read(buffer)) > 0) {
//                writer.write(buffer, 0, len);
//            }

            byte[] buffer = new byte[13];
            inflate.read(buffer);
            writer.write(buffer);

            // Decompress the rest of the file
            buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inflate.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}