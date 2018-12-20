package coder;

import app.Header;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

public class MockCoder extends Coder {
    @Override
    protected Header getHeader() {
        return Header.HUFFMAN_STANDARD;
    }

    @Override
    protected void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException {
        InputStream inputStream = inputStreamSupplier.get();
        int b;
        int counter = 0;
        while ((b = inputStream.read()) != -1) {
            out.write(b);
            counter++;
        }
        inputStream.close();
        System.out.println("Compressed total bytes: " + counter);
    }

    @Override
    protected void decompress(InputStream in, OutputStream out) throws IOException {
        int b;
        int counter = 0;
        while ((b = in.read()) != -1) {
            out.write(b);
            counter++;
        }
        in.close();
        System.out.println("Decompressed total bytes: " + counter);
    }
}
