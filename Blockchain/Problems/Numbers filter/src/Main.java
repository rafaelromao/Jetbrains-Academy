
class NumbersFilter extends Thread {

    /* use it to read numbers from the standard input */
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            var n = scanner.nextInt();
            if (n  == 0) {
                break;
            }
            if (n % 2 == 0) {
                System.out.println(n);
            }
        }
    }
}