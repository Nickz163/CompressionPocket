package coder;

import app.Header;
import javafx.util.Pair;
import utils.ComplexPath;
import utils.MultipartOutputStream;
import utils.func.Helpers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class Coder {
    private static final String DECOMPRESSED_PATH = "decompressed";

    protected abstract Header getHeader();

    protected abstract void compress(Supplier<InputStream> inputStreamSupplier, OutputStream out) throws IOException;

    protected abstract void decompress(InputStream in, OutputStream out) throws IOException;

    public void checkHeader(String fileName) throws IOException {
        HeaderProcessor.checkHeader(fileName, getHeader());
    }

    public void compress(List<String> fileNames, String archiveName) throws IOException {
        try (OutputStream out = openFile(archiveName);
             DataOutputStream dos = new DataOutputStream(out)
        ) {
            HeaderProcessor.writeHeader(dos, getHeader());
            writeFilesInfo(fileNames, dos);
            compress(readAndCombine(fileNames), out);
        }
    }

    private InputStream singleInputStream(String fileName) throws IOException {
        return Files.newInputStream(Paths.get(fileName));
    }

    private Supplier<InputStream> readAndCombine(List<String> fileNames) {
        return () -> {
            Stream<InputStream> fileStreams = getFileTree(fileNames)
                    .filter(ComplexPath::isFile)
                    .map(Helpers.unchecked(ComplexPath::open));
            return new SequenceInputStream(utils.Helpers.asEnum(fileStreams));
        };
    }

    private void writeFilesInfo(List<String> fileNames, DataOutputStream dos) throws IOException {
        dos.writeInt((int) getFileTree(fileNames).count());
        getFileTree(fileNames)
                .forEach(Helpers.uncheckedC(path -> {
                    if (path.isFile()) {
                        dos.writeLong(path.size());
                        System.out.println("Compressing file: " + path.relativeString());
                    } else {
                        dos.writeLong(0);
                        System.out.println("Reading directory: " + path.relativeString());
                    }
                    dos.writeUTF(path.relativeString());
                }));
    }

    private Stream<ComplexPath> getFileTree(List<String> fileNames) {
        return fileNames.stream()
                .map(Paths::get)
                .map(Path::normalize)
                .map(ComplexPath::fromAbsolute)
                .flatMap(Helpers.unchecked(ComplexPath::walk))
                .distinct();
    }

    public void decompress(String fileName) throws IOException {
        try (DataInputStream dis = new DataInputStream(singleInputStream(fileName))) {
            HeaderProcessor.checkHeader(dis, getHeader());
            Files.createDirectories(Paths.get(DECOMPRESSED_PATH));

            int elementsAmount = dis.readInt();
            Map<Boolean, List<Pair<Long, String>>> filesAndDirs = IntStream.range(0, elementsAmount)
                    .boxed()
                    .map(Helpers.unchecked(i -> new Pair<>(dis.readLong(), dis.readUTF())))
                    .collect(Collectors.partitioningBy(p -> p.getKey() == 0));

            filesAndDirs.get(true)
                    .forEach(Helpers.uncheckedC(dir -> {
                        System.out.println("Creating directory: " + dir.getValue());
                        Files.createDirectories(Paths.get(DECOMPRESSED_PATH, dir.getValue()));
                    }));

            List<Pair<Long, OutputStream>> openFiles = filesAndDirs.get(false).stream()
                    .peek(file -> System.out.println("Decompressing file: " + file.getValue()))
                    .map(Helpers.unchecked(file ->
                            new Pair<>(
                                    file.getKey(),
                                    openFile(DECOMPRESSED_PATH + "/" + file.getValue())
                            ))
                    )
                    .collect(Collectors.toList());

            try (MultipartOutputStream out = new MultipartOutputStream(openFiles)) {
                decompress(dis, out);
            }
        }
    }

    private OutputStream openFile(String fileName) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(fileName));
    }

    public void showContent(String fileName) throws IOException {
        try (DataInputStream dis = new DataInputStream(singleInputStream(fileName))) {
            HeaderProcessor.checkHeader(dis, getHeader());

            int fileAmount = dis.readInt();
            for (int i = 0; i < fileAmount; i++) {
                dis.readLong();
                System.out.println(dis.readUTF());
            }
        }
    }
}
