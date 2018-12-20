package arithmetic;/*
 * Reference arithmetic coding
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/reference-arithmetic-coding
 * https://github.com/nayuki/Reference-arithmetic-coding
 */

import java.io.*;
import java.util.function.Supplier;


/**
 * Compression application using adaptive arithmetic coding.
 * <p>Usage: java AdaptiveArithmeticCompress InputFile OutputFile</p>
 * <p>Then use the corresponding "AdaptiveArithmeticDecompress" application to recreate the original input file.</p>
 * <p>Note that the application starts with a flat frequency table of 257 symbols (all set to a frequency of 1),
 * and updates it after each byte encoded. The corresponding decompressor program also starts with a flat
 * frequency table and updates it after each byte decoded. It is by design that the compressor and
 * decompressor have synchronized states, so that the data can be decompressed properly.</p>
 */
public class AdaptiveArithmeticCompress {
	
	public static void main(String[] args) throws IOException {
		// Handle command line arguments
		if (args.length != 2) {
			System.err.println("Usage: java AdaptiveArithmeticCompress InputFile OutputFile");
			System.exit(1);
			return;
		}
		File inputFile  = new File(args[0]);
		File outputFile = new File(args[1]);
		
		// Perform file compression
		try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
				BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
			compress(in, out);
		}
	}

	public static void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException {
		try (InputStream in = inputStreamSupplier.get();
			 BitOutputStream bitOut = new BitOutputStream(out)) {
			compress(in, bitOut);
		}
	}

	// To allow unit testing, this method is package-private instead of private.
	static void compress(InputStream in, BitOutputStream out) throws IOException {
		FlatFrequencyTable initFreqs = new FlatFrequencyTable(257);
		FrequencyTable freqs = new SimpleFrequencyTable(initFreqs);
		ArithmeticEncoder enc = new ArithmeticEncoder(32, out);
		while (true) {
			// Read and encode one byte
			int symbol = in.read();
			if (symbol == -1)
				break;
			enc.write(freqs, symbol);
			freqs.increment(symbol);
		}
		enc.write(freqs, 256);  // EOF
		enc.finish();  // Flush remaining code bits
	}
	
}
