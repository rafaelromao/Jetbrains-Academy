package analyzer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String UNKNOWN_FILE_TYPE = "Unknown file type";

    public static void main(String[] args) {
        var fileName = args[0];
        var pattern = args[1];
        var type = args[2];
        var analyzer = new Analyzer();
        try (var file = new FileInputStream(fileName)) {
            var isMatch = analyzer.isMatch(file, pattern);
            System.out.println(isMatch ? type : UNKNOWN_FILE_TYPE);

            List l1 = new ArrayList<>();
            List l2 = new ArrayList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
