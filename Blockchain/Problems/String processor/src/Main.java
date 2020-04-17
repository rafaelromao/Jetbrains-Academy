
class StringProcessor extends Thread {

    final Scanner scanner = new Scanner(System.in); // use it to read string from the standard input

    @Override
    public void run() {
        while (true) {
            var word = scanner.nextLine();
            var upper = word.toUpperCase();
            if (!word.equals(upper)) {
                System.out.println(upper);
                continue;
            }
            System.out.println("FINISHED");
            break;
        }
    }
}