package huffman;

import java.util.Objects;

interface Node {
    class Internal implements Node {
        final Node leftChild;  // Not null
        final Node rightChild;  // Not null

        Internal(Node left, Node right) {
            leftChild = Objects.requireNonNull(left);
            rightChild = Objects.requireNonNull(right);
        }
    }
    class Leaf implements Node {
        final int symbol;  // Always non-negative

        Leaf(int sym) {
            if (sym < 0)
                throw new IllegalArgumentException("Symbol header must be non-negative");
            symbol = sym;
        }
    }
}