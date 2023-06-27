package com.kbki.decompressor;

public class DecompressingFailedException extends Exception {
    String message;

    DecompressingFailedException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Decompressing is failed. " + message;
    }
}
