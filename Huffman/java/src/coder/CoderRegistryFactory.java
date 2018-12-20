package coder;

import coder.impl.AdaptiveArithmeticCoder;
import coder.impl.AdaptiveHuffmanCoder;
import coder.impl.ArithmeticCoder;
import coder.impl.HuffmanCoder;

public class CoderRegistryFactory {
    public static CoderRegistry create() {
        return new CoderRegistry()
                .withCoder("-hs", new HuffmanCoder())
                .withCoder("-ha", new AdaptiveHuffmanCoder())
                .withCoder("-as", new ArithmeticCoder())
                .withCoder("-aa", new AdaptiveArithmeticCoder());
    }
}
