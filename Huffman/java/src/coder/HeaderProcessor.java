package coder;

import app.Header;

import java.io.*;
import java.util.Objects;

public class HeaderProcessor {
    public static void writeHeader(DataOutputStream os, Header header) throws IOException {
        os.writeUTF(header.value);
    }

    public static void checkHeader(DataInputStream is, Header header) throws IOException {
        try {
            String readHeader = is.readUTF();
            if (!Objects.equals(readHeader, header.value)) {
                throw new IOException(String.format("Bad header: expected %s, found %s", header.value, readHeader));
            }
        } catch (UTFDataFormatException e) {
            throw new IOException("Invalid header format");
        }
    }

    public static void checkHeader(String fileName, Header header) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileName);
             DataInputStream dis = new DataInputStream(fis)) {
            checkHeader(dis, header);
        }
    }
}