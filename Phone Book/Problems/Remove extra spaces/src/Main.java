import java.util.Scanner;

class RemoveExtraSpacesProblem {
    public static void main(String[] args) {
        try(var scanner = new Scanner(System.in)) {
            var text = scanner.nextLine();
            System.out.println(text.replaceAll("\\s+"," "));
        }
    }
}