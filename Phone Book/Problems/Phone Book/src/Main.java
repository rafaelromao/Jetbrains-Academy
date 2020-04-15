import java.util.Scanner;

public class Main {
    private static class TableEntry<T> {
        private final int key;
        private final T value;
        private boolean removed;

        public TableEntry(int key, T value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        public void remove() {
             removed = true;
        }

        public boolean isRemoved() {
             return removed;
        }
    }

    private static class HashTable<T> {
        private int size;
        private TableEntry[] table;

        public HashTable(int size) {
            this.size = size;
            table = new TableEntry[size];
        }

        public boolean put(int key, T value) {
            int idx = findKey(key);

            if (idx == -1) {
                return false;
            }

            table[idx] = new TableEntry(key, value);
            return true;
        }

        public T get(int key) {
            int idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return null;
            }

            return (T) table[idx].getValue();
        }

        public void remove(int key) {
            int idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return;
            }

            table[idx] = null;
        }

        private int findKey(int key) {
            int hash = key % size;

            while (!(table[hash] == null || table[hash].getKey() == key)) {
                hash = (hash + 1) % size;

                if (hash == key % size) {
                    return -1;
                }
            }

            return hash;
        }
    }

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var table = new HashTable<String>(n);
            for (var i = 0; i < n; i++) {
                var query = scanner.next();
                switch (query) {
                    case "put":
                        table.put(scanner.nextInt(), scanner.next().strip());
                        break;
                    case "get":
                        var value = table.get(scanner.nextInt());
                        value = value == null ? "-1" : value;
                        System.out.println(value);
                        break;
                    case "remove":
                        table.remove(scanner.nextInt());
                        break;
                }
            }
        }
    }
}