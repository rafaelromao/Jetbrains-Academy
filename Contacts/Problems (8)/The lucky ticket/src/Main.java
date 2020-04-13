import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var ticket = scanner.nextLine();
            var start = 0;
            var end = 0;
            for (var i = 0; i < 3; i++) {
                var s = ticket.charAt(i);
                var e = ticket.charAt(ticket.length()-i-1);
                start += Character.getNumericValue(s);
                end += Character.getNumericValue(e);
            }
            System.out.println(start == end ? "Lucky" : "Regular");
        }
    }
}