package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ComplexPath {
    private final Path absolute;
    private final Path relative;

    public ComplexPath(Path absolute, Path relative) {
        this.absolute = absolute;
        this.relative = relative;
    }

    private ComplexPath(Path path) {
        absolute = path;
        relative = path.subpath(path.getNameCount() - 1, path.getNameCount());
    }

    public static ComplexPath fromAbsolute(Path path) {
        return new ComplexPath(path);
    }

    public ComplexPath relativise(Path path) {
        return new ComplexPath(path, relative.resolve(absolute.relativize(path)));
    }

    public Stream<ComplexPath> walk() throws IOException {
        return Files.walk(absolute)
                .map(this::relativise);
    }

    public boolean isFile() {
        return Files.isRegularFile(absolute);
    }

    public long size() {
        try {
            return Files.size(absolute);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream open() throws IOException {
        return Files.newInputStream(this.absolute);
    }

    public String relativeString() {
        return relative.toString();
    }

    public String absoluteString() {
        return absolute.toString();
    }
}
