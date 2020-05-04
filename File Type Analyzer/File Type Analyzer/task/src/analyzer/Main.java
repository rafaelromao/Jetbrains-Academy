package analyzer;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    private static final String UNKNOWN_FILE_TYPE = "Unknown file type";

    public static void main(String[] args) {
        var fileName = args[0];
        var pattern = args[1];
        var type = args[2];
        var analyzer = new Analizer();
        try (var file = new FileInputStream(fileName)) {
            var isMatch = analyzer.isMatch(file, pattern);
            System.out.println(isMatch ? type : UNKNOWN_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
