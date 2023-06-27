package com.kbki.compressor;

public class KBKI {
    private byte version;
    private int height;
    private int width;
    private byte filter;
    private byte colorType;
    private byte compressionType;
    private byte encryptionType;
    private byte encryptionKeyLength;
    private byte[] encryptionKey;

    // array of lines with filter and pixels
    private byte[][] lines;

    // TODO make check for array size!
    // if we have all data in 1 array
    public void setMetadata(byte[] bytes) {
        this.version = bytes[0];

        // get int number from array of bytes
        this.height = 0;
        this.height += ((bytes[1] & 0xFF) << 8 * 3);
        this.height += ((bytes[2] & 0xFF) << 8 * 2);
        this.height += ((bytes[3] & 0xFF) << 8);
        this.height += ((bytes[4] & 0xFF));

        this.width = bytes[0];
        this.width += ((bytes[5] & 0xFF) << 8 * 3);
        this.width += ((bytes[6] & 0xFF) << 8 * 2);
        this.width += ((bytes[7] & 0xFF) << 8);
        this.width += ((bytes[8] & 0xFF));

        this.filter = bytes[9];
        this.colorType = bytes[10];
        this.compressionType = bytes[11];
        this.encryptionType = bytes[12];
        this.encryptionKeyLength = bytes[13];

        // entry encryption key
        this.encryptionKey = new byte[bytes.length - 13];
        for (int i = 14, j = 0; i < bytes.length; ++i, ++j) {
            this.encryptionKey[j] = bytes[i];
        }
    }

    // if we have one more array for encryption key
    // and one more variable for encryption key length
    public void setMetadata(byte[] bytes, byte encryptionKeyLength, byte[] encryptionKey) {
        this.version = bytes[0];

        // get int number from array of bytes
        this.height = 0;
        this.height += ((bytes[1] & 0xFF) << 8 * 3);
        this.height += ((bytes[2] & 0xFF) << 8 * 2);
        this.height += ((bytes[3] & 0xFF) << 8);
        this.height += ((bytes[4] & 0xFF));

        this.width = bytes[0];
        this.width += ((bytes[5] & 0xFF) << 8 * 3);
        this.width += ((bytes[6] & 0xFF) << 8 * 2);
        this.width += ((bytes[7] & 0xFF) << 8);
        this.width += ((bytes[8] & 0xFF));

        this.filter = bytes[9];
        this.colorType = bytes[10];
        this.compressionType = bytes[11];
        this.encryptionType = bytes[12];
        this.encryptionKeyLength = encryptionKeyLength;

        // entry encryption key
        this.encryptionKey = new byte[bytes.length - 13];
        for (int i = 14, j = 0; i < bytes.length; ++i, ++j) {
            this.encryptionKey[j] = bytes[i];
        }
    }

    // no recommended use. typical use convert byte after byte
    public void setLines(byte[] bytes) {
        // one more for filter byte
        this.lines = new byte[height][width + 1];
        int bytesCounter = 0;
        for(int i = 0; i < height; ++i ) {
            for (int j = 0; j <= width; ++j) {
                if (bytesCounter < bytes.length) {
                    this.lines[i][j] = bytes[bytesCounter];
                    bytesCounter++;
                }
            }
        }
    }

    public byte getCompressionType() {
        return this.compressionType;
    }

    public byte getEncryptionType() {
        return encryptionType;
    }

    public byte getEncryptionKeyLength() {
        return encryptionKeyLength;
    }
}
