class NumbersThread extends Thread {

    private final int from;
    private final int to;

    public NumbersThread(int from, int to) {
        this.from = from;
        this.to = to;
        setName("NumbersThread");
    }

    @Override
    public void run() {
        for (var i = from; i <= to; i++) {
            System.out.println(i);
        }
    }
}