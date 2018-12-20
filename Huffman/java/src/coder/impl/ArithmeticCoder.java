package coder.impl;

import app.Header;
import arithmetic.ArithmeticCompress;
import arithmetic.ArithmeticDecompress;
import coder.Coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

public class ArithmeticCoder extends Coder {
    @Override
    protected Header getHeader() {
        return Header.ARITHMETIC_STANDARD;
    }

    @Override
    protected void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException {
        ArithmeticCompress.compress(inputStreamSupplier, out);
    }

    @Override
    protected void decompress(InputStream in, OutputStream out) throws IOException {
        ArithmeticDecompress.decompress(in, out);
    }
}
