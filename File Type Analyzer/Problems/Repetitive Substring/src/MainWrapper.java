import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainWrapper {
    public static void main(String[] args) throws IOException {
        FileInputStream is = new FileInputStream(new File("/Users/rromao/Downloads/hyperskill-9636-test-01.txt"));
        System.setIn(is);
        Main.main(args);
    }
}