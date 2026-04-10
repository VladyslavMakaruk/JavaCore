package org.example.client;
import org.example.connectionsStubs.FakeConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("clientTest")
public class ClientTest {

    static String fileName = "clientdata.json";
    static String filePath = System.getProperty("user.dir") + "/src/client/data/";
    static String host = "localhost";
    static int port = 5000;

    private void writeStringDataToFile(String filepath, String filename, String data) throws IOException{
        if (!Files.exists(Path.of(filepath))){
            Files.createDirectories(Path.of(filepath));
            if(!Files.exists(Path.of(filepath+filename))){
                Files.createFile(Path.of(filepath+filename));
            }
        }
        try (Writer writer = Files.newBufferedWriter(Path.of(filepath+filename))) {
            writer.write(data);
        }
    }

    @BeforeEach
    void setup() throws IOException {
        Path pathToFile = Path.of(filePath + fileName);
        if(Files.exists(pathToFile)){
            Files.delete(pathToFile);
        }
    }

    @AfterAll
    static void cleanData() throws IOException {
        Path pathToFile = Path.of(filePath + fileName);
        if(Files.exists(pathToFile)){
            Files.delete(pathToFile);
        }
    }

    static Stream<Arguments> validArguments() {
        return Stream.of(
                Arguments.of(
                        (Object) new String[]{
                                "-t",
                                "get",
                                "-k",
                                "[\"path1\",\"path2\"]"
                        }
                ),
                Arguments.of(
                        (Object) new String[]{
                                "-t",
                                "set",
                                "-k",
                                "key",
                                "-v",
                                "value"
                        }
                ),
                Arguments.of(
                        (Object) new String[]{
                                "-t",
                                "exit"
                        }
                ),
                Arguments.of(
                        (Object) new String[]{
                                "-t",
                                "delete",
                                "-k",
                                "key",
                        }
                )
        );
    }

    @ParameterizedTest(name = "valid arguments test \n {0}", autoCloseArguments = false)
    @MethodSource("validArguments")
    void handleValidInput(
            String[] arguments
    ) throws IOException {
        FakeConnection fakeConnection = new FakeConnection("any non null String");
        Client client = new Client(
                arguments,
                host,
                port,
                filePath,
                (host,port) -> fakeConnection
        );
        client.start();
        assertNotNull(fakeConnection.getLastSentMessage());
    }


    static Stream<Arguments> invalidArguments() {
        return Stream.of(
                Arguments.of(
                        (Object) new String[]{
                                "-t",
                                "get some"
                        }
                ),
                Arguments.of(
                        (Object) new String[]{
                                "-t",
                                "set",
                                "-k",
                                "value"
                        }
                )
        );
    }

    @ParameterizedTest(name = "invalid arguments test \n {0}", autoCloseArguments = false)
    @MethodSource("invalidArguments")
    void handleInvalidValidInput(
            String[] arguments
    ) throws IOException {
        FakeConnection fakeConnection = new FakeConnection(null);
        Client client = new Client(
                arguments,
                host,
                port,
                filePath+fileName,
                (host,port) -> fakeConnection
        );
        client.start();
        assertNull(fakeConnection.getLastSentMessage());
    }

    static Stream<Arguments> argsFromFile() {
        return Stream.of(
                Arguments.of(
                        "{\"type\":\"set\",\"key\":\"person1\",\"value\":\"field\"}"
                ),
                Arguments.of(
                        "{\"type\":\"get\",\"key\":\"person1\"}"
                ),
                Arguments.of(
                        "{\"type\":\"exit\"}"
                ),
                Arguments.of(
                        "{\"type\":\"delete\",\"key\":\"person1\"}"
                )
        );
    }

    @ParameterizedTest(name="parse args from file \n {0}",autoCloseArguments = false)
    @MethodSource("argsFromFile")
    void parseArgsFromFile(
        String jsonData
    ) throws IOException {
        writeStringDataToFile(filePath,fileName,jsonData);
        FakeConnection fakeConnection = new FakeConnection(null);
        Client client = new Client(
                new String[] {"-in",fileName},
                host,
                port,
                filePath,
                (host,port) -> fakeConnection
        );
        client.start();
        assertEquals(jsonData,fakeConnection.getLastSentMessage());
    }
}
