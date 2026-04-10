package org.example.util;

import com.google.gson.JsonObject;
import org.example.util.inputValidation.ArgsValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Arguments Deserializer Test")
public class ArgsDeserializerTest {
    private final ArgsDeserializer argsDeserial = new ArgsDeserializer();

    static JsonObject jsonCreation(String[] input){
        JsonObject json = new JsonObject();
        json.addProperty("type",input[0]);
        json.addProperty("key",input[1]);
        json.addProperty("value",input[2]);
        return json;
    }

    static Stream<Arguments> deserializationInput() {
        return Stream.of(
                Arguments.of(
                        jsonCreation(new String[]{"set","key","value"})
                ),
                Arguments.of(
                        jsonCreation(new String[]{"get","key",null})
                ),
                Arguments.of(
                        jsonCreation(new String[]{"delete","key",null})
                ),
                Arguments.of(
                        jsonCreation(new String[]{"exit",null,null})
                )
        );
    }


    @ParameterizedTest(name = "Arguments Deserialization \n {0} \n {1}", autoCloseArguments = false)
    @MethodSource("deserializationInput")
    void DeserializationTest(
            JsonObject json
    ){
        Args arguments = argsDeserial.deserialize(json,null,null);
        assertTrue(ArgsValidator.validateArgs(arguments));
    }
}
