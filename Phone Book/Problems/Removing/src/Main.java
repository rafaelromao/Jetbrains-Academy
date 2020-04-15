import java.util.*;

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
            var idx = findKey(key);

            if (idx == -1) {
                rehash();
                idx = findKey(key);
            }

            table[idx] = new TableEntry(key, value);

            return true;
        }

        public T get(int key) {
            var idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return null;
            }

            return (T) table[idx].getValue();
        }

        public void remove(int key) {
            var idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return;
            }

            table[idx].remove();
        }

        private int findKey(int key) {
            var hash = key % size;

            while (!(table[hash] == null || table[hash].getKey() == key)) {

                hash = (hash + 1) % size;

                if (hash == key % size) {
                    return -1;
                }
            }

            return hash;
        }

        private void rehash() {
            size = size * 2;
            var old = table;
            table = new TableEntry[size];
            for (var entry: old) {
                put(entry.getKey(), (T) entry.getValue());
            }
        };

        @Override
        public String toString() {
            var tableStringBuilder = new StringBuilder();

            for (var i = 0; i < table.length; i++) {
                if (table[i] == null) {
                    tableStringBuilder.append(i + ": null");
                } else {
                    tableStringBuilder.append(i + ": key=" + table[i].getKey()
                            + ", value=" + table[i].getValue()
                            + ", removed=" + table[i].isRemoved());
                }

                if (i < table.length - 1) {
                    tableStringBuilder.append("\n");
                }
            }

            return tableStringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var m = scanner.nextInt();
            var table = new HashTable<String>(5);
            for (var i = 0; i < n; i++) {
                table.put(scanner.nextInt(), scanner.nextLine().strip());
            }
            for (var i = 0; i < m; i++) {
                table.remove(scanner.nextInt());
            }
            System.out.println(table);
        }
    }
}