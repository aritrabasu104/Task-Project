package com.example.cs.demo.reader.impl;

import com.example.cs.demo.reader.Reader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class FileReader implements Reader<String> {
    @Override
    public Stream<String> readData(Path path) throws IOException {
        return Files.newBufferedReader(path).lines();
    }
}
