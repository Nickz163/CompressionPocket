package coder.impl;

import app.Header;
import arithmetic.AdaptiveArithmeticCompress;
import arithmetic.AdaptiveArithmeticDecompress;
import coder.Coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

public class AdaptiveArithmeticCoder extends Coder {
    @Override
    protected Header getHeader() {
        return Header.ARITHMETIC_ADAPTIVE;
    }

    @Override
    protected void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException {
        AdaptiveArithmeticCompress.compress(inputStreamSupplier, out);
    }

    @Override
    protected void decompress(InputStream in, OutputStream out) throws IOException {
        AdaptiveArithmeticDecompress.decompress(in, out);
    }
}
