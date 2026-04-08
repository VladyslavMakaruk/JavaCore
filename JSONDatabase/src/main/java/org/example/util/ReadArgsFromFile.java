package org.example.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadArgsFromFile {
    String filePath;
    public ReadArgsFromFile(String filename){
        this.filePath  = System.getProperty("user.dir")
                + "/src/client/data/" + filename;
    }
    public String argsInJsonFormat() throws IOException {
        return Files.readString(Path.of(filePath));
    }
}
