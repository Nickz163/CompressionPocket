package utils.func;

public interface CheckedFunction<A, B> {
    B apply(A a) throws Throwable;
}
