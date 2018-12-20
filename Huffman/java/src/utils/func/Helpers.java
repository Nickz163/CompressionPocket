package utils.func;

import java.util.function.Consumer;
import java.util.function.Function;

public class Helpers {

    public static <A, B> Function<A, B> unchecked(CheckedFunction<A, B> fun) {
        return a -> {
            try {
                return fun.apply(a);
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        };
    }

    public static <A> Consumer<A> uncheckedC(CheckedConsumer<A> fun) {
        return a -> {
            try {
                fun.apply(a);
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        };
    }
}

