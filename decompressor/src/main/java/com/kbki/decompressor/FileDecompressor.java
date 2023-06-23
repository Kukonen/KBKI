import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public interface FileDecompresser {
    void decompressToFile(String sourceFileName, String resultFileName);
    void decompressAndShowImage(String sourceFileName) throws DecompressingFailedException;
    ArrayList<String> decompress(FileInputStream fileInputStream) throws DecompressingFailedException, IOException;
}
