public class Demo {
    public static void main(String[] args) {
        KBKIDecompresser decompresser = new KBKIDecompresser();

        try {
            decompresser.decompressAndShowImage("test2.txt");
        } catch (DecompressingFailedException e) {
            e.printStackTrace();
        }
    }
}
