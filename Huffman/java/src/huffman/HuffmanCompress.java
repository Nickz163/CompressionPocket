package huffman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;


/**
 * Compression application using static Huffman coding.
 * <p>Usage: java huffman.HuffmanCompress InputFile OutputFile</p>
 * <p>Then use the corresponding "huffman.HuffmanDecompress" application to recreate the original input file.</p>
 * <p>Note that the application uses an alphabet of 257 symbols - 256 symbols for the byte values
 * and 1 symbol for the EOF marker. The compressed file format starts with a list of 257
 * code lengths, treated as a canonical code, and then followed by the Huffman-coded data.</p>
 */
public final class HuffmanCompress {

    // Command line main application function.
    public static void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException {

        // Read input file once to compute symbol frequencies.
        // The resulting generated code is optimal for static Huffman coding and also canonical.
        InputStream is = inputStreamSupplier.get();
        FrequencyTable freqs = getFrequencies(is);
        is.close();

        freqs.increment(256);  // EOF symbol gets a frequency of 1
        CodeTree code = freqs.buildCodeTree();
        CanonicalCode canonCode = new CanonicalCode(code, freqs.getSymbolLimit());
        // Replace code tree with canonical one. For each symbol,
        // the code header may change but the code length stays the same.
        code = canonCode.toCodeTree();

        // Read input file again, compress with Huffman coding, and write output file
        try (BitOutputStream bitOut = new BitOutputStream(out)) {
            writeCodeLengthTable(bitOut, canonCode);
            InputStream in = inputStreamSupplier.get();
            compress(code, in, bitOut);
            in.close();
        }
    }


    // Returns a frequency table based on the bytes in the given file.
    // Also contains an extra entry for symbol 256, whose frequency is set to 0.
    private static FrequencyTable getFrequencies(InputStream is) throws IOException {
        FrequencyTable freqs = new FrequencyTable(new int[257]);
        while (true) {
            int b = is.read();
            if (b == -1)
                break;
            freqs.increment(b);
        }
        is.close();
        return freqs;
    }


    static void writeCodeLengthTable(BitOutputStream out, CanonicalCode canonCode) throws IOException {
        for (int i = 0; i < canonCode.getSymbolLimit(); i++) {
            int val = canonCode.getCodeLength(i);
            // For this file format, we only support codes up to 255 bits long
            if (val >= 256)
                throw new RuntimeException("The code for a symbol is too long");

            // Write header as 8 bits in big endian
            for (int j = 7; j >= 0; j--)
                out.write((val >>> j) & 1);
        }
    }


    // To allow unit testing, this method is package-private instead of private.
    static void compress(CodeTree code, InputStream in, BitOutputStream out) throws IOException {
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = code;
        while (true) {
            int b = in.read();
            if (b == -1)
                break;
            enc.write(b);
        }
        in.close();
        enc.write(256);  // EOF
    }

}
