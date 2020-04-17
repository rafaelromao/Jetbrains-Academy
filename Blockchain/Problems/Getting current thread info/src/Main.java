public static void printCurrentThreadInfo() {
    Thread t = Thread.currentThread(); // main thread
    System.out.println("name: " + t.getName());
    System.out.println("priority: " + t.getPriority());
}
