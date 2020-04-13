class ComparatorInspector {

	public static <T> java.lang.reflect.Type getComparatorType(Class<? extends Comparable<T>> clazz) {
		return java.util.stream.Stream
				.of(clazz.getGenericInterfaces())
                .filter(java.lang.reflect.ParameterizedType.class::isInstance)
				.filter(gi -> ((java.lang.reflect.ParameterizedType) gi).getRawType() == Comparable.class)
				.map(gi -> ((java.lang.reflect.ParameterizedType) gi).getActualTypeArguments()[0])
				.findAny()
				.orElse(null);
	}

}