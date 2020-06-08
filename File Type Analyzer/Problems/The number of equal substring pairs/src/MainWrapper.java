import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainWrapper {
    public static void main(String[] args) throws IOException {
        FileInputStream is = new FileInputStream(new File("/Users/rromao/Downloads/hyperskill-5723-test-06.txt"));
        System.setIn(is);
        Main.main(args);
    }
}