import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            scanner.nextLine();
            var a = new int[n];
            for (var i = 0; i < n; i++) {
                a[i] = scanner.nextInt();
            }
            var r = countShifts(a);
            System.out.println(r);
        }
    }

    public static int countShifts(int[] array) {
        var count = 0;
        for (var i = array.length - 1; i >= 0; i--) {
            for (var j = i - 1; j >= 0; j--) {
                if (array[j] < array[i]) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}