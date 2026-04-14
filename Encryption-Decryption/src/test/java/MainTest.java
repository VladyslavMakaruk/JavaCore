import org.example.Main;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("test main")
public class MainTest {

    static Stream<Arguments> testMainInput() {
        return Stream.of(
                Arguments.of(
                        new String[]{
                            "-data","abcdABCD",
                            "-alg","shift",
                            "-key","10",
                            "-mode","enc"
                        },
                        "klmnKLMN"
                ),
                Arguments.of(
                        new String[]{
                            "-data","efghEFGH",
                            "-alg","shift",
                            "-key","30",
                            "-mode","dec"
                        },
                        "abcdABCD"
                ),
                Arguments.of(
                        new String[]{
                            "-data","abcd",
                            "-alg","unicode",
                            "-key","100",
                            "-mode","enc"
                        },
                        "ÅÆÇÈ"
                ),
                Arguments.of(
                        new String[]{
                            "-data","щъыь",
                            "-alg","unicode",
                            "-key","1000",
                            "-mode","dec"
                        },
                        "abcd"
                )
        );
    }


    @ParameterizedTest(name = "test main: {1}")
    @MethodSource("testMainInput")
    void testFindPersonMethod(
            String[] args,
            String expectedOutput
    ) throws FileNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        Main.main(args);
        assertEquals(expectedOutput,outputStream.toString().trim());
    }

}
