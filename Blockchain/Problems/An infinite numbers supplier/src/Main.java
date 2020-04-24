import java.util.function.*;

class FunctionUtils {

    public static Supplier<Integer> getInfiniteRange() {
        return new Supplier<Integer>() {
            private int counter = 0;
            public Integer get() {
                return counter++;
            }
        };
    }

}
