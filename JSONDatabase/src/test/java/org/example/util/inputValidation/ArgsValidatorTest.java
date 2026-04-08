package org.example.util.inputValidation;

import com.beust.jcommander.JCommander;
import org.example.util.Args;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;

@DisplayName("Arguments validator test")
public class ArgsValidatorTest {

    private Args parseArgs(String[] inputArgs) {
        Args parsedArgs = new Args();
        JCommander.newBuilder()
                .addObject(parsedArgs)
                .build()
                .parse(inputArgs);
        return parsedArgs;
    }

    static Stream<Arguments> getInputs() {
        return Stream.of(
                Arguments.of(new String[]{"-t", "GET"}, false),
                Arguments.of(new String[]{"-t", "GET", "-k", ""}, false),
                Arguments.of(new String[]{"-t", "GeT", "-k", "2500"}, true),
                Arguments.of(new String[]{"-t", "get", "-k", "test"}, true)
        );
    }

    static Stream<Arguments> deleteInputs() {
        return Stream.of(
                Arguments.of(new String[]{"-t", "DELETE"}, false),
                Arguments.of(new String[]{"-t", "delete", "-k", ""}, false),
                Arguments.of(new String[]{"-t", "delete", "-k", "2500"}, true),
                Arguments.of(new String[]{"-t", "DELETE", "-k", "test"}, true)
        );
    }

    static Stream<Arguments> setInputs() {
        return Stream.of(
                Arguments.of(new String[]{"-t", "SET"}, false),
                Arguments.of(new String[]{"-t", "Set", "-k", ""}, false),
                Arguments.of(new String[]{"-t", "Set", "-k", "100", "-v", ""}, false),
                Arguments.of(new String[]{"-t", "Set", "-k", "100"}, false),
                Arguments.of(new String[]{"-t", "SeT", "-k", "2500", "-v", "123"}, true),
                Arguments.of(new String[]{"-t", "set", "-k", "<key>", "-v", "some value"}, true)
        );
    }

    static Stream<Arguments> exitInputs() {
        return Stream.of(
                Arguments.of(new String[]{"-t", "exit"}, true),
                Arguments.of(new String[]{"-t", "eXIt", "-v", "unused"}, true),
                Arguments.of(new String[]{"-t", "exit", "-k", "key", "-v", "value"}, true)
        );
    }

    @ParameterizedTest(name = "GET: args={0} -> {1}", autoCloseArguments = false)
    @MethodSource("getInputs")
    void testGetValidation(String[] inputArgs, boolean expected) {
        assertEquals(expected, ArgsValidator.validateArgs(parseArgs(inputArgs)));
    }

    @ParameterizedTest(name = "DELETE: args={0} -> {1}", autoCloseArguments = false)
    @MethodSource("deleteInputs")
    void testDeleteValidation(String[] inputArgs, boolean expected) {
        assertEquals(expected, ArgsValidator.validateArgs(parseArgs(inputArgs)));
    }

    @ParameterizedTest(name = "SET: args={0} -> {1}", autoCloseArguments = false)
    @MethodSource("setInputs")
    void testSetValidation(String[] inputArgs, boolean expected) {
        assertEquals(expected, ArgsValidator.validateArgs(parseArgs(inputArgs)));
    }

    @ParameterizedTest(name = "EXIT: args={0} -> {1}", autoCloseArguments = false)
    @MethodSource("exitInputs")
    void testExitValidation(String[] inputArgs, boolean expected) {
        assertEquals(expected, ArgsValidator.validateArgs(parseArgs(inputArgs)));
    }
}
