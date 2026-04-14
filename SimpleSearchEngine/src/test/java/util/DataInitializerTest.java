package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static util.DataInitializer.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Data initializer (util class) test")
public class DataInitializerTest {
    private final String filename = "testDataFile.test";

    private void writeStringDataToFile(String filename, String data) throws IOException {
        Path path = Path.of(filename);
        if(!Files.exists(path)){
            Files.createFile(path);
        }
        try (Writer writer = Files.newBufferedWriter(path)) {
            writer.write(data);
        }
    }

    @BeforeEach
    void setup() throws IOException {
        Path pathToFile = Path.of(this.filename);
        if(Files.exists(pathToFile)){
            Files.delete(pathToFile);
        }
    }

    static Stream<Arguments> testReadDataFromFile() {
        return Stream.of(
                Arguments.of(
                        "data1\ndata2\ndata3"
                ),
                Arguments.of(
                        "testData  se\ntestdata2"
                )
        );
    }

    @ParameterizedTest(name = "read data {0} \n filename: {1}")
    @MethodSource("testReadDataFromFile")
    void testAllStrategy(String inputString) throws IOException {
        this.writeStringDataToFile(this.filename,inputString);
        ArrayList<String> expectedData = new ArrayList<>(List.of(inputString.split("\n")));
        ArrayList<String> result = new ArrayList<>();
        readDataFromFile(result,filename);
        Collections.sort(expectedData);
        Collections.sort(result);
        assertEquals(expectedData, result);
    }
}

