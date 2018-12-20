package coder.impl;

import app.Header;
import coder.Coder;
import huffman.HuffmanCompress;
import huffman.HuffmanDecompress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

public class HuffmanCoder extends Coder {

    @Override
    protected Header getHeader() {
        return Header.HUFFMAN_STANDARD;
    }

    @Override
    protected void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException {
        HuffmanCompress.compress(inputStreamSupplier, out);
    }

    @Override
    protected void decompress(InputStream in, OutputStream out) throws IOException {
        HuffmanDecompress.decompress(in, out);
    }
}
