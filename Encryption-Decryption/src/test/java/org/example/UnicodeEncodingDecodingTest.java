package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Unicode encoding decoding test")
public class UnicodeEncodingDecodingTest {

    static Stream<Arguments> testEncodingInput() {
        return Stream.of(
                Arguments.of(
                        10,
                        "abcdABCD",
                        "klmnKLMN"
                ),
                Arguments.of(
                        100,
                        "abcd",
                        "ÅÆÇÈ"
                ),
                Arguments.of(
                        1000,
                        "abcd",
                        "щъыь"
                )
        );
    }

    @ParameterizedTest(name = "test encoding | step={0} | input=\"{1}\" | expected=\"{2}\"")
    @MethodSource("testEncodingInput")
    public void testEncoding(
            int step,
            String stringToEncode,
            String expectedResult
    ) {
        UnicodeEncodingDecoding encoder = new UnicodeEncodingDecoding(step);
        String encodedString = encoder.encode(stringToEncode);
        assertEquals(expectedResult, encodedString);
    }

    static Stream<Arguments> testDecodingInput() {
        return Stream.of(
                Arguments.of(
                        10,
                        "klmnKLMN",
                        "abcdABCD"
                ),
                Arguments.of(
                        100,
                        "ÅÆÇÈ",
                        "abcd"
                ),
                Arguments.of(
                        1000,
                        "щъыь",
                        "abcd"
                )
        );
    }

    @ParameterizedTest(name = "test decoding | step={0} | input=\"{1}\" | expected=\"{2}\"")
    @MethodSource("testDecodingInput")
    public void testDecoding(
            int step,
            String stringToDecode,
            String expectedResult
    ) {
        UnicodeEncodingDecoding decoder = new UnicodeEncodingDecoding(step);
        String decodedString = decoder.decode(stringToDecode);
        assertEquals(expectedResult, decodedString);
    }
}