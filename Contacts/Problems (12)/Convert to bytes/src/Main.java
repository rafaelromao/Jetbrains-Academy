import java.io.InputStream;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = System.in;
        var bytes = inputStream.readAllBytes();
        for (var b: bytes) {
            System.out.print(b);
        }
    }
}