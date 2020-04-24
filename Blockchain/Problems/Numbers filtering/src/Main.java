public static IntStream createFilteringStream(IntStream evenStream, IntStream oddStream) {
        return IntStream.concat(evenStream, oddStream)
        .filter(i -> i % 3 == 0 && i % 5 == 0)
        .sorted()
        .distinct()
        .skip(2);
}