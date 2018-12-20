package utils;

import utils.func.Helpers;
import javafx.util.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MultipartOutputStream extends OutputStream {
    List<Pair<Long, OutputStream>> outputStreams;
    long offset = 0;
    int streamNumber = 0;

    public MultipartOutputStream(List<Pair<Long, OutputStream>> outputStreams) {
        this.outputStreams = outputStreams;
    }

    @Override
    public void write(int b) throws IOException {
        if (streamNumber < outputStreams.size()) {
            long size = outputStreams.get(streamNumber).getKey();
            if (offset < size) {
                offset++;
            } else {
                offset = 1;
                streamNumber++;
            }
            outputStreams.get(streamNumber).getValue().write(b);
        } else {
            throw new RuntimeException("Exhausted underlying streams in MultipartOutputStream");
        }
    }

    @Override
    public void close() throws IOException {
        outputStreams.forEach(Helpers.uncheckedC(p -> p.getValue().close()));
    }
}
