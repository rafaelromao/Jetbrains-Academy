import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var dna = scanner.nextLine();
            var output = "";
            var counter = 0;
            for (var i = 0; i < dna.length(); i++) {
                var next = dna.charAt(i);
                // For first char
                if (i == 0) {
                    output += next;
                }
                // For any character
                var current = output.charAt(output.length()-1);
                if (current == next) {
                    counter++;
                } else {
                    output += counter;
                    output += next;
                    counter = 1;
                }
                // For last char
                if (i == dna.length()-1) {
                    output += counter;
                }
            }
            System.out.println(output);
        }
    }
}