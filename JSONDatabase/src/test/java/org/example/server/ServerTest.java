package org.example.server;
import com.google.gson.JsonObject;
import org.example.client.Client;
import org.example.util.Args;
import org.example.util.ServerResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.example.util.JsonConvertor.*;


@DisplayName("serverTest")
public class ServerTest {
    private Server server;
    private String fileName = "internal.json";
    private String filePath = System.getProperty("user.dir") + "/src/client/data/";
    private String host = "localhost";
    private final int port = 5000;

    @BeforeEach
    void setup() throws IOException {
        Path pathToFile = Path.of(filePath + fileName);
        if(Files.exists(pathToFile)){
            Files.delete(pathToFile);
        }
        server = new Server(filePath,fileName,port);
    }

    public static JsonObject nestedJson(){
        JsonObject lower = new JsonObject();
        lower.addProperty("key", "value");
        JsonObject middle = new JsonObject();
        middle.add("Bob", lower);
        JsonObject upper = new JsonObject();
        upper.add("Data",middle);
        return upper;
    }

    static Stream<Arguments> addRetrieve() {
        return Stream.of(
                Arguments.of(
                        new String[]{"set","key","value"},
                        new String[]{"get","key",null}
                ),
                Arguments.of(
                        new String[]{
                                "set",
                                Arrays.toString(
                                    new String[]{
                                            "lvl1",
                                            "lvl2",
                                            "lvl3"
                                    }
                                ),
                                "value"},
                        new String[]{
                                "get",
                                Arrays.toString(
                                    new String[]{
                                        "lvl1",
                                        "lvl2",
                                        "lvl3"
                                    }
                                ),
                                null}
                ),
                Arguments.of(
                        new String[]{
                                "set",
                                Arrays.toString(
                                        new String[]{
                                                "lvl1-xxx21",
                                                "lvl2",
                                                "lvl3"
                                        }
                                ),
                                elementToString(nestedJson())},
                        new String[]{
                                "get",
                                Arrays.toString(
                                        new String[]{
                                                "lvl1-xxx21",
                                                "lvl2",
                                                "lvl3"
                                        }
                                ),
                                null}
                )
        );
    }

    @ParameterizedTest(name = "ADD - RETRIEVE \n {0} \n {1}", autoCloseArguments = false)
    @MethodSource("addRetrieve")
    void TestCaseAddRetrieve(
        String[] setArg,
        String[] getArg
    ){
        Args request = new Args();
        initializeRequest(request,setArg);
        ServerResponse response = server.handleRequest(request);
        assertEquals(response.getResponse(),ServerResponse.validResponse);
        initializeRequest(request,getArg);
        response = server.handleRequest(request);
        assertEquals(response.getResponse(),ServerResponse.validResponse);
    }

    private void initializeRequest(Args request, String[] setArg){
        request.setCommandType(setArg[0]);
        request.setCommandKey(setArg[1]);
        request.setCommandMessage(setArg[2]);
    }
}
