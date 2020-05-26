import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            var n = scanner.nextInt();
            var aa = new int[n][];
            var t = 0;
            var m = 0;
            for (var i = 0; i < n; i++) {
                var a = new int[scanner.nextInt()];
                var l = a.length;
                for (var j = 0; j < l; j++) {
                    a[j] = scanner.nextInt();
                }
                aa[i] = a;
                t += l;
                m = l > m ? l : m;
            }
            var r = new int[t];
            var l = 0;
            for (var i = 0; i < n; i++) {
                l = merge(aa[i], r, l);
            }
            for (var i = 0; i < t; i++) {
                System.out.printf("%d ", r[i]);
            }
        }
    }

    private static int merge(int[] next, int[] array, int filled) {
        var middle = next.length;
        var right = filled;
        var i = 0;
        var j = 0;
        var k = 0;

        var temp = new int[filled + middle];

        while (i < middle && j < right) {
            if (next[i] >= array[j]) {
                temp[k] = next[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }

        for (; i < middle; i++, k++) {
            temp[k] = next[i];
        }

        for (; j < right; j++, k++) {
            temp[k] = array[j];
        }

        System.arraycopy(temp, 0, array, 0, temp.length);

        return temp.length;
    }
}