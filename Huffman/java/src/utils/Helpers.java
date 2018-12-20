package utils;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Stream;

public class Helpers {
    public static  <T> Enumeration<T> asEnum(Stream<T> stream) {
        return new Enumeration<T>() {
            private final Iterator<T> i = stream.iterator();

            public boolean hasMoreElements() {
                return i.hasNext();
            }

            public T nextElement() {
                return i.next();
            }
        };
    }
}
