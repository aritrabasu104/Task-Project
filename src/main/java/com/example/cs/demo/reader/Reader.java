package com.example.cs.demo.reader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface Reader<T> {
    Stream<T> readData(Path path) throws IOException;
}
