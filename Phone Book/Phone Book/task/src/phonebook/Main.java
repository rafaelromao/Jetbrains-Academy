package phonebook;

import java.io.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Main {
    private static AtomicInteger search = new AtomicInteger();
    private static AtomicInteger found = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        var phones = "C:\\Users\\rafae\\Downloads\\directory.txt";
        var find = "C:\\Users\\rafae\\Downloads\\find.txt";

        try (var phonesStream = new FileInputStream(phones);
             var phonesReader = new InputStreamReader(phonesStream);
             var phonesBufferedReader = new BufferedReader(phonesReader);
             var findFile = new FileInputStream(find);
             var findReader = new InputStreamReader(findFile);
             var findBufferedReader = new BufferedReader(findReader)) {

            var phoneLines = phonesBufferedReader.lines().collect(toList());
            var findLines = findBufferedReader.lines().collect(toList());
            var hashTable = new HashMap<String, String>();

            var linearSearchTime = search("linear search",
                    null,
                    null,
                    () -> linearSearch(phoneLines, findLines),
                    null);

            search("bubble sort + jump search",
                    null,
                    () -> bubbleSort(phoneLines, linearSearchTime * 10),
                    () -> jumpSearch(phoneLines, findLines),
                    () -> linearSearch(phoneLines, findLines));

            search("quick sort + binary search",
                    null,
                    () -> quickSort(phoneLines),
                    () -> binarySearch(phoneLines, findLines),
                    null);

            search("hash table",
                    () -> populateHashTable(phoneLines, hashTable),
                    null,
                    () -> hashtableSearch(hashTable, findLines),
                    null);
        }
    }

    private static void populateHashTable(List<String> phoneLines, Map<String, String> hashTable) {
        hashTable.putAll(phoneLines
                .stream()
                .collect(toMap(s -> getOwnersName(s), s -> getPhoneNumber(s))));
    }

    private static void hashtableSearch(Map<String, String> hashTable, List<String> findLines) {
        for (var findLine : findLines) {
            search.getAndIncrement();
            var result = hashTable.getOrDefault(findLine, null);
            if (result != null) {
                found.getAndIncrement();
            }
        }
    }

    private static void binarySearch(List<String> phoneLines, List<String> findLines) {
        var array = phoneLines.toArray(new String[0]);
        for (var findLine : findLines) {
            search.getAndIncrement();
            var result = Arrays.binarySearch(array, findLine, Main::compareByOwnersName);
            if (result > -1) {
                found.getAndIncrement();
            }
        }
    }

    private static boolean quickSort(List<String> array) {
        quickSort(array, 0, array.size() - 1);
        return true;
    }

    private static void quickSort(List<String> array, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(array, left, right);
            quickSort(array, left, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, right);
        }
    }

    private static int partition(List<String> array, int left, int right) {
        var pivot = array.get(right);
        var partitionIndex = left;

        for (var i = left; i < right; i++) {
            if (compareByOwnersName(array.get(i), pivot) < 0) {
                swap(array, i, partitionIndex++);
            }
        }

        swap(array, partitionIndex, right);

        return partitionIndex;
    }

    private static void swap(List<String> array, int i, int j) {
        var temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }

    private static void jumpSearch(List<String> phoneLines, List<String> findLines) {
        var jump = (int) Math.floor(Math.sqrt(phoneLines.size()));
        for (var findLine : findLines) {
            var previous = 0;
            search.getAndIncrement();
            for (var next = 0; next < phoneLines.size(); next += jump) {
                if (next > phoneLines.size() - 1) {
                    next = phoneLines.size() - 1;
                }
                var phoneLine = phoneLines.get(next);
                if (compareByOwnersName(phoneLine, findLine) == 0) {
                    found.getAndIncrement();
                    break;
                }
                if (compareByOwnersName(phoneLine, findLine) < 0) {
                    for (var phoneLineInBlock : phoneLines.subList(previous, next)) {
                        if (phoneLineInBlock.contains(findLine)) {
                            found.getAndIncrement();
                            break;
                        }
                    }
                }
                previous = next;
            }
        }
    }

    private static int compareByOwnersName(String phoneLine, String findLine) {
        var first = getOwnersName(phoneLine);
        var second = getOwnersName(findLine);
        return first.compareTo(second);
    }

    private static Pattern pattern = Pattern.compile("(\\d* ?)(.*)");

    private static String getOwnersName(String phoneLine) {
        var matcher = pattern.matcher(phoneLine);
        matcher.matches();
        return matcher.group(2).strip();
    }

    private static String getPhoneNumber(String phoneLine) {
        var matcher = pattern.matcher(phoneLine);
        matcher.matches();
        return matcher.group(1).strip();
    }

    private static boolean bubbleSort(List<String> phoneLines, long timeLimitInMillis) {
        var start = System.currentTimeMillis();
        var swap = true;
        for (var i = 0; i < phoneLines.size() && swap; i++) {
            swap = false;
            for (var j = 0; j < phoneLines.size() - i - 1; j++) {
                if (System.currentTimeMillis() - start > timeLimitInMillis) {
                    return false;
                }
                var first = phoneLines.get(j);
                var second = phoneLines.get(j + 1);
                if (compareByOwnersName(first, second) > 0) {
                    phoneLines.set(j + 1, first);
                    phoneLines.set(j, second);
                    swap = true;
                }
            }
        }
        return true;
    }

    private static void linearSearch(List<String> phoneLines, List<String> findLines) {
        for (var findLine : findLines) {
            search.getAndIncrement();
            for (var phoneLine : phoneLines) {
                if (phoneLine.contains(findLine)) {
                    found.getAndIncrement();
                    break;
                }
            }
        }
    }

    private static long search(
            String caption,
            Runnable creationAction,
            BooleanSupplier sortingAction,
            Runnable searchingAction,
            Runnable fallbackSearchingAction) {
        found.set(0);
        search.set(0);
        var start = System.currentTimeMillis();

        Duration creationTime = null;
        if (creationAction != null) {
            var creationStart = System.currentTimeMillis();
            creationAction.run();
            var creationEnd = System.currentTimeMillis();
            creationTime = Duration.ofMillis(creationEnd - creationStart);
        }

        Duration sortTime = null;
        boolean couldSort = true;
        if (sortingAction != null) {
            var sortStart = System.currentTimeMillis();
            couldSort = sortingAction.getAsBoolean();
            var sortEnd = System.currentTimeMillis();
            sortTime = Duration.ofMillis(sortEnd - sortStart);
        }

        System.out.printf("Start searching (%s)\n", caption);
        if (couldSort) {
            searchingAction.run();
        } else {
            fallbackSearchingAction.run();
        }

        var end = System.currentTimeMillis();
        var time = Duration.ofMillis(end - start);
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.\n",
                found.get(), search.get(), time.toMinutesPart(), time.toSecondsPart(), time.toMillisPart());

        if (creationAction != null) {
            System.out.printf("Creating time: %d min. %d sec. %d ms.\n",
                    creationTime.toMinutesPart(), creationTime.toSecondsPart(), creationTime.toMillisPart());
        }
        if (sortingAction != null) {
            System.out.printf("Sorting time: %d min. %d sec. %d ms.\n",
                    sortTime.toMinutesPart(), sortTime.toSecondsPart(), sortTime.toMillisPart());
        }
        if (creationAction != null || sortingAction != null) {
            var prepTime = creationTime != null ? creationTime : sortTime;
            var searchingTime = Duration.ofMillis(time.toMillis() - prepTime.toMillis());
            System.out.printf("Searching time: %d min. %d sec. %d ms.\n",
                    searchingTime.toMinutesPart(), searchingTime.toSecondsPart(), searchingTime.toMillisPart());
        }

        System.out.println();

        return time.toMillis();
    }
}
