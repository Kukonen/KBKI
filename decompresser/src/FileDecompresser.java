import java.io.FileReader;
import java.util.ArrayList;

public interface FileDecompresser {
    void decompressToFile(String sourceFileName, String resultFileName);
    void decompressAndShowImage(String sourceFileName);
    ArrayList<String> decompress();
}
