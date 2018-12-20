package coder;

import java.util.HashMap;
import java.util.Map;

public class CoderRegistry {
    private final Map<String, Coder> coders = new HashMap<>();

    public CoderRegistry withCoder(String consoleParam, Coder coder) {
        coders.put(consoleParam, coder);
        return this;
    }

    public Coder get(String consoleParam) {
        return coders.get(consoleParam);
    }
}
