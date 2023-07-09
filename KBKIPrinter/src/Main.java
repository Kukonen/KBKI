public class Main {
    public static void main(String[] args) {
        KBKIPrinter decompressor = new KBKIPrinter();
        try {
            decompressor.showImageFromDecompressedFile("imagePixels.kbki");
        } catch (InvalidFileStructureException e) {
            throw new RuntimeException(e);
        }
    }
}