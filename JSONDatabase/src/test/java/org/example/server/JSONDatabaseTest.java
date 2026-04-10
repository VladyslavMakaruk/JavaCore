package org.example.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.database.JSONDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.example.util.JsonConvertor.*;


@DisplayName("Json database test")
public class JSONDatabaseTest {

    private JSONDatabase database;
    String fileName = "testData.json";
    String filePath = System.getProperty("user.dir") + "/src/client/data/";

    @BeforeEach
    void setup() throws IOException {
        Path pathToFile = Path.of(filePath + fileName);
        if(Files.exists(pathToFile)){
            Files.delete(pathToFile);
        }
        database = new JSONDatabase(filePath,fileName);
    }

    @Test
    @DisplayName("is data file created after db initialized")
    void testFileCreationAfterInitialization(){
        Path pathToFile = Path.of(filePath + fileName);
        assertTrue(Files.exists(pathToFile));
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
                        new String[]{"Person1"},
                        "{\"name\":\"Alice\",\"age\":30}",
                        new String[]{"Person1","name"},
                        "Alice"
                ),
                Arguments.of(
                        new String[]{"Person1"},
                        "{\"name\":\"Alice\",\"age\":\"30\"}",
                        new String[]{"Person1","age"},
                        "30"
                ),
                Arguments.of(
                        new String[]{"Person1","data"},
                        "{\"name\":\"Alice\",\"age\":\"30\"}",
                        new String[]{"Person1","data","age"},
                        "30"
                ),
                Arguments.of(
                        new String[]{"input"},
                        elementToString(nestedJson()),
                        new String[]{"input","Data","Bob","key"},
                        "value"
                )
        );
    }

    @ParameterizedTest(name = "ADD - RETRIEVE \n {0} \n {1} \n {2} \n {3}", autoCloseArguments = false)
    @MethodSource("addRetrieve")
    void TestCaseAddRetrieve(
        String[] key,
        String value,
        String[] pathToValue,
        String expectedValue
    ){
        database.setByPath(
                getStringAsJsonArray(Arrays.toString(key)),
                parseStringToJson(value)
        );
        JsonElement result = database.getByJsonArray(
                getStringAsJsonArray(Arrays.toString(pathToValue))
        );
        assertEquals(expectedValue, elementToString(result));
    }

    static Stream<Arguments> addDeleteRetrieve() {
        return Stream.of(
                Arguments.of(
                        new String[]{"Person1"},
                        "{\"name\":\"Alice\",\"age\":30}",
                        new String[]{"Person1","name"},
                        true,
                        new String[]{"Person1","age"},
                        "30"
                ),
                Arguments.of(
                        new String[]{"Person1"},
                        "{\"name\":\"Alice\",\"age\":\"30\"}",
                        new String[]{"Person1","age"},
                        true,
                        new String[]{"Person1","name"},
                        "Alice"
                ),
                Arguments.of(
                        new String[]{"Person1"},
                        "{\"name\":\"Alice\",\"age\":\"30\"}",
                        new String[]{"Person1"},
                        true,
                        new String[]{"Person1","age"},
                        null
                )
        );
    }

    @ParameterizedTest(name = "ADD - DELETE - RETRIEVE \n {0} \n {1} \n {2} \n {3} \n {4}", autoCloseArguments = false)
    @MethodSource("addDeleteRetrieve")
    void TestCaseAddUpdateRetrieve(
            String[] key,
            String value,
            String[] deleteKey,
            boolean deleteResponse,
            String[] pathToValue,
            String expectedValue
    ){
        database.setByPath(
                getStringAsJsonArray(Arrays.toString(key)),
                parseStringToJson(value)
        );
        assertEquals(
                database.deleteByPath(getStringAsJsonArray(Arrays.toString(deleteKey))),
                deleteResponse
        );
        JsonElement result = database.getByJsonArray(
                getStringAsJsonArray(Arrays.toString(pathToValue))
        );
        assertEquals(expectedValue, expectedValue != null ? elementToString(result): result);
    }
}
