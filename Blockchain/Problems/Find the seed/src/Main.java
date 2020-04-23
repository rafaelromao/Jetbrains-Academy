import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var a = scanner.nextInt();
            var b = scanner.nextInt();
            var n = scanner.nextInt();
            var k = scanner.nextInt();
            var seed = Integer.MAX_VALUE;
            var minMax = Integer.MAX_VALUE;
            for (var localSeed = a; localSeed <= b; localSeed++) {
                var random = new Random(localSeed);
                var localMinMax = 0;
                for (var i = 0; i < n; i++) {
                    var current = random.nextInt(k);
                    if (current > localMinMax) {
                        localMinMax = current;
                    }
                }
                if (localMinMax < minMax || (localMinMax == minMax && localSeed < seed)) {
                    minMax = localMinMax;
                    seed = localSeed;
                }
            }
            System.out.println(seed);
            System.out.println(minMax);
        }
    }
}