//interface Copy<T> {
//	T copy();
//}
//
//class Folder<T> {
//	private T item;
//
//	public void put(T item) {
//		this.item = item;
//	}
//
//	public T get() {
//		return this.item;
//	}
//}


/**
 * Class to work with
 */
class Multiplicator {

	public static <T extends Copy<T>> Folder<T>[] multiply(Folder<T> folder, int arraySize) {
		var objects = new java.util.ArrayList<Folder<T>>();
		for (var i = 0; i < arraySize; i++) {
			var copyItem = folder.get().copy();
			var copyFolder = new Folder<T>();
			copyFolder.put(copyItem);
			objects.add(copyFolder);
		}
		return objects.toArray(new Folder[0]);
	}

}