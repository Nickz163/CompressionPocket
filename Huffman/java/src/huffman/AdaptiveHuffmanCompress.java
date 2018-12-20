package huffman;

import java.io.*;
import java.util.Arrays;
import java.util.function.Supplier;


/**
 * Compression application using adaptive Huffman coding.
 * <p>Usage: java huffman.AdaptiveHuffmanCompress InputFile OutputFile</p>
 * <p>Then use the corresponding "huffman.AdaptiveHuffmanDecompress" application to recreate the original input file.</p>
 * <p>Note that the application starts with a flat frequency table of 257 symbols (all set to a frequency of 1),
 * collects statistics while bytes are being encoded, and regenerates the Huffman code periodically. The
 * corresponding decompressor program also starts with a flat frequency table, updates it while bytes are being
 * decoded, and regenerates the Huffman code periodically at the exact same points in time. It is by design that
 * the compressor and decompressor have synchronized states, so that the data can be decompressed properly.</p>
 */
public final class AdaptiveHuffmanCompress {

    // Command line main application function.
    public static void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException {

        try (BitOutputStream bitOut = new BitOutputStream(out)) {
            compress(inputStreamSupplier.get(), bitOut);
        }
    }


    public static void compress(InputStream in, BitOutputStream out) throws IOException {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqs = new FrequencyTable(initFreqs);
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = freqs.buildCodeTree();  // Don't need to make canonical code because we don't transmit the code tree
        int count = 0;  // Number of bytes read from the input file
        while (true) {
            // Read and encode one byte
            int symbol = in.read();
            if (symbol == -1)
                break;
            enc.write(symbol);
            count++;

            // Update the frequency table and possibly the code tree
            freqs.increment(symbol);
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                enc.codeTree = freqs.buildCodeTree();
            if (count % 262144 == 0)  // Reset frequency table
                freqs = new FrequencyTable(initFreqs);
        }
        enc.write(256);  // EOF
    }


    private static boolean isPowerOf2(int x) {
        return x > 0 && Integer.bitCount(x) == 1;
    }

}
