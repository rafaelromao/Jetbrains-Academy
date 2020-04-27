public static long rangeQuadraticSum(int fromIncl, int toExcl) {
        return IntStream.range(fromIncl, toExcl).reduce(0, (x, y) -> x + y * y);
}