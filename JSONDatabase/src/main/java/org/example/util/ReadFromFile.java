package org.example.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFromFile {

    public static String readString(String filePath, String filename) throws IOException {
        return Files.readString(Path.of(filePath+filename));
    }
}
