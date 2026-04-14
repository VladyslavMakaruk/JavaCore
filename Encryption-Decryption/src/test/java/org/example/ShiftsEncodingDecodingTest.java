package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Shifts encoding decoding test")
public class ShiftsEncodingDecodingTest {

    static Stream<Arguments> testEncodingInput() {
        return Stream.of(
                Arguments.of(
                       10,
                        "abcdABCD",
                        "klmnKLMN"
                ),
                Arguments.of(
                       20,
                        "abcdABCD",
                        "uvwxUVWX"
                ),
                Arguments.of(
                       30,
                        "abcdABCD",
                        "efghEFGH"
                )
        );
    }

    @ParameterizedTest(name = "test encoding\n input: {0}\n output: {1}")
    @MethodSource("testEncodingInput")
    public void testEncoding(
            int step,
            String stringToEncode,
            String expectedResult
    ){
        ShiftsEncodingDecoding encoder = new ShiftsEncodingDecoding(step);
        String encodedString = encoder.encode(stringToEncode);
        assertEquals(encodedString,expectedResult);
    }

    static Stream<Arguments> testDecodingInput() {
        return Stream.of(
                Arguments.of(
                        10,
                        "klmnKLMN",
                        "abcdABCD"
                ),
                Arguments.of(
                        20,
                        "uvwxUVWX",
                        "abcdABCD"
                ),
                Arguments.of(
                        30,
                        "efghEFGH",
                        "abcdABCD"
                )
        );
    }

    @ParameterizedTest(name = "test decoding\n input: {0}\n output: {1}")
    @MethodSource("testDecodingInput")
    public void testDecoding(
            int step,
            String stringToDecode,
            String expectedResult
    ){
        ShiftsEncodingDecoding decoder = new ShiftsEncodingDecoding(step);
        String encodedString = decoder.decode(stringToDecode);
        assertEquals(encodedString,expectedResult);
    }
}
