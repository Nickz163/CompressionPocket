package utils.func;

public interface CheckedConsumer<A> {
    void apply(A a) throws Throwable;
}
