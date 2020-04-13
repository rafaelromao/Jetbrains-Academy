class QualityControl {

	public static boolean check(List<Box<? extends Bakery>> boxes) {
		if (!boxes.isEmpty()) {
			for (var i = 0; i < boxes.size(); i++) {
				if (!(boxes.get(i) instanceof Box))
					return false;
				if (!(boxes.get(i).get() instanceof Bakery))
					return false;
			}
		}
		return true;
	}

}