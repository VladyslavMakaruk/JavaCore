package org.example.server;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.example.connectionsStubs.FakeConnection;
import org.example.connectionsStubs.IServerStub;
import org.example.database.JSONDatabase;
import org.example.util.Args;
import org.example.util.ServerResponse;
import org.example.util.inputValidation.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.example.util.JsonConvertor.*;


@DisplayName("serverTest")
public class ServerTest {
    private Server server;
    public static String fileName = "internal.json";
    public static String filePath = System.getProperty("user.dir") + "/src/client/data/";
    public static int PORT = 5001;
    private final int port = 5000;

    @BeforeEach
    void setup() throws IOException {
        Path pathToFile = Path.of(filePath + fileName);
        if(Files.exists(pathToFile)){
            Files.delete(pathToFile);
        }
        server = new Server(filePath,fileName,port,(port) -> new IServerStub(new String[]{"test"}));
    }

    private Args initializeRequest(String[] setArg){
        Args request = new Args();
        request.setCommandType(setArg[0]);
        request.setCommandKey(setArg[1]);
        request.setCommandMessage(setArg[2]);
        return request;
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

    static Stream<Arguments> setRetrieve() {
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


    //TODO: compare exact return value
    @ParameterizedTest(name = "SET - RETRIEVE \n {0} \n {1}", autoCloseArguments = false)
    @MethodSource("setRetrieve")
    void TestCaseAddRetrieve(
        String[] setArg,
        String[] getArg
    ) {
        Args request;
        request = initializeRequest(setArg);
        ServerResponse response = server.handleRequest(request);
        assertEquals(response.getResponse(),ServerResponse.validResponse);
        request = initializeRequest(getArg);
        response = server.handleRequest(request);
        assertEquals(response.getResponse(),ServerResponse.validResponse);
    }

    static Stream<Arguments> setDelete() {
        return Stream.of(
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
                        "value"
                    },
                    new String[]{
                            "delete",
                            Arrays.toString(
                                    new String[]{
                                            "lvl1-xxx21",
                                            "lvl2",
                                            "lvl3"
                                    }
                            ),
                            null
                    },
                    ServerResponse.validResponse
                ),
                Arguments.of(
                        new String[]{
                                "set",
                                "key",
                                "value"
                        },
                        new String[]{
                                "delete",
                                "key",
                                null
                        },
                        ServerResponse.validResponse
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
                                "value"
                        },
                        new String[]{
                                "delete",
                                Arrays.toString(
                                        new String[]{
                                                "lvl1-xxx21",
                                                "lvl2",
                                                "lvl4"
                                        }
                                ),
                                null
                        },
                        ServerResponse.invalidResponse
                )
        );
    }

    @ParameterizedTest(name = "SET - DELETE \n {0} \n {1} \n {2}", autoCloseArguments = false)
    @MethodSource("setDelete")
    void TestCaseSetDelete(
            String[] setArg,
            String[] deleteArg,
            String serverResponseOnDelete
    ){
        Args request;
        request = initializeRequest(setArg);
        ServerResponse response = server.handleRequest(request);
        assertEquals(ServerResponse.validResponse,response.getResponse());
        request = initializeRequest(deleteArg);
        response = server.handleRequest(request);
        assertEquals(serverResponseOnDelete,response.getResponse());
    }

    @Test
    @DisplayName("test exit command")
    void TestExitCommand(){
        boolean isShutDown = server.isShutDown();
        assertFalse(isShutDown);
        Args request = new Args();
        request.setCommandType(Command.EXIT.toString());
        ServerResponse response = server.handleRequest(request);
        assertEquals(response.getResponse(),ServerResponse.validResponse);
        isShutDown = server.isShutDown();
        assertTrue(isShutDown);
    }

    @ParameterizedTest(name = "test invalid commandType: {0}")
    @ValueSource(strings = {
            "get1",
            " get 1",
            "exit from",
            "delete from",
            "Sset"
    })
    void TestInvalidCommand(String commandType){
        Args request = new Args();
        request.setCommandType(commandType);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> server.handleRequest(request)
        );
        assertEquals("invalid command type",exception.getMessage());
    }

    static Stream<Arguments> commandsList() {
        return Stream.of(
                Arguments.of(
                        new String[]{
                                "{\"type\":\"invalid\"}",
                                "{\"type\":\"set\",\"key\":\"person1\",\"value\":\"field\"}",
                                "{\"type\":\"get\",\"key\":\"person1\"}",
                                "{\"type\":\"delete\",\"key\":\"person1\"}",
                                "{\"type\":\"get\",\"key\":\"person1\"}",
                                "{\"type\":\"exit\"}"
                        },
                        new ServerResponse[]{
                                new ServerResponse(ServerResponse.invalidResponse,null,"invalid request"),
                                new ServerResponse(ServerResponse.validResponse),
                                new ServerResponse(ServerResponse.validResponse,new JsonPrimitive("field")),
                                new ServerResponse(ServerResponse.validResponse),
                                new ServerResponse(ServerResponse.invalidResponse,null, JSONDatabase.noKey),
                                new ServerResponse(ServerResponse.validResponse),
                        }
                )
        );
    }
    @ParameterizedTest(name = "test server behavior}",autoCloseArguments = false)
    @MethodSource("commandsList")
    void TestHandleConnection(String[] commandsList,ServerResponse[] expectedResponse) throws IOException {
        try (var serverStub = new IServerStub(commandsList)){
            List<FakeConnection> connectionList = serverStub.returnConnectionList();
            Server server = new Server(filePath,fileName,PORT,(port) -> serverStub);
            server.Start();
            for(int i =0; i < commandsList.length;i++){
                assertEquals(
                        connectionList.get(i).getLastSentMessage(),
                        ServerResponse.serverResponseToJson(expectedResponse[i])
                );
            }
        }
    }

}
