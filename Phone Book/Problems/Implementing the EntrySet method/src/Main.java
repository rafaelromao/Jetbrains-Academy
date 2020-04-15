import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static class TableEntry<T> {
        private final int key;
        private T value;

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

            var entryValue = table[idx] == null
                    ? value.toString()
                    : table[idx].getValue() + " " + value.toString();
            table[idx] = new TableEntry(key, entryValue);
            return true;
        }

        public T get(int key) {
            int idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return null;
            }

            return (T) table[idx].getValue();
        }

        public Set<TableEntry<List>> entrySet() {
            var map = new HashMap<Integer, TableEntry<List>>();
            for (var entry: table) {
                if (entry == null) {
                    continue;
                }
                var key = entry.getKey();
                if (!map.containsKey(key)) {
                    map.put(key, new TableEntry<>(key, new ArrayList()));
                }
                var values = map.get(key).getValue();
                values.add(entry.getValue());
            }
            return map.values().stream().collect(Collectors.toSet());
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

        private void rehash() {
            // put your code here
        }
    }

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var table = new HashTable<String>(n);
            for (var i = 0; i < n; i++) {
                table.put(scanner.nextInt(), scanner.nextLine().strip());
            }
            for (var entry : table.entrySet()) {
                var value = entry.getValue()
                        .stream()
                        .map(i -> i.toString())
                        .reduce((s1, s2) -> s1 + " " + s2);
                System.out.printf("%d: %s\n", entry.getKey(), value.get());
            }
        }
    }
}