package coder.impl;

import app.Header;
import coder.Coder;
import huffman.AdaptiveHuffmanCompress;
import huffman.AdaptiveHuffmanDecompress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

public class AdaptiveHuffmanCoder extends Coder {

    @Override
    public Header getHeader() {
        return Header.HUFFMAN_ADAPTIVE;
    }

    @Override
    protected void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException {
        AdaptiveHuffmanCompress.compress(inputStreamSupplier, out);
    }

    @Override
    protected void decompress(InputStream in, OutputStream out) throws IOException {
        AdaptiveHuffmanDecompress.decompress(in, out);
    }
}
