package app;

public enum Header {
    HUFFMAN_STANDARD("X_HUFFMAN_COMPRESSED"),
    HUFFMAN_ADAPTIVE("X_ADAPTIVE_HUFFMAN_COMPRESSED"),
    ARITHMETIC_STANDARD("X_ARITHMETIC_COMPRESSED"),
    ARITHMETIC_ADAPTIVE("X_ARITHMETIC_ADAPTIVE");

    public final String value;

    Header(String value) {
        this.value = value;
    }
}
