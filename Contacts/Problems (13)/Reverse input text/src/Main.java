import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

class Main {
    public static void main(String[] args) throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            var line = reader.readLine();
            var chars = line.toCharArray();
            for (int i = chars.length-1; i >= 0; i--) {
                char c = chars[i];
                System.out.print(c);
            }
        }
    }
}