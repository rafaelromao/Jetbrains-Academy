import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (var scanner = new java.util.Scanner(System.in)) {
            var s = scanner.nextLine();
            var t = scanner.nextLine();
            var r = new ArrayList<Integer>();
            if ("".equals(s)) {
                r.add(0);
            } else {
                for (var i = 0; i < t.length() - s.length() + 1; i++) {
                    var match = true;
                    for (var j = 0; j < s.length(); j++) {
                        if (t.charAt(i + j) != s.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        r.add(i);
                    }
                }
            }
            System.out.println(r.size());
            r.forEach(i -> System.out.print(i + " "));
        }
    }
}