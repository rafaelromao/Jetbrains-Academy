import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;

class Main {
    public static void main(String[] args) throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            var words = Stream.of(reader.readLine().split(" "))
                    .map(s -> s.strip())
                    .filter(s -> !s.isEmpty())
                    .count();
            System.out.print(words);
        }
    }

}