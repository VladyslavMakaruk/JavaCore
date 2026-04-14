
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

@DisplayName("User menu test")
public class UserMenuTest {
    private final InputStream inputStream = System.in;
    private final int defaultNumberOfOptions = 3;
    private UserMenu userMenu;
    private final ArrayList<String> data = new ArrayList<>(List.of(
            "Andrew Ukraine Kyiv 000",          //0
            "Roman USA New Jersey 001",        //1
            "Mark USA Phoenix 010",             //2
            "Julia Spain Madrid 011",           //3
            "Linus Finland Helsinki 100",       //4
            "Vladyslav Ukraine Ternopil 101",   //5
            "Kateryna Ukraine Rivne 110",       //6
            "Roman Poland Wroclaw 111"          //7
    ));

    UserMenuTest() throws FileNotFoundException {
        this.userMenu = new   UserMenu(defaultNumberOfOptions,data);
    }

    static Stream<Arguments> testFindPersonMethod() {
        return Stream.of(
                Arguments.of("ANY",
                        "Ukraine USA",
                        new Integer[]{0,1,2,5,6}
                ),
                Arguments.of(
                        "NONE",
                        "Ukraine USA Madrid 100",
                        new Integer[]{7}
                ),
                Arguments.of(
                        "ALL",
                        "China",
                        null
                )
        );
    }

    @ParameterizedTest(name = "search query: {0}, indexes: {1}")
    @MethodSource("testFindPersonMethod")
    void testFindPersonMethod(
            String matchingStrategy,
            String searchQuery,
            Integer[] requiredResult
    ) throws FileNotFoundException {
        String input = String.format("%s\n%s\n",matchingStrategy,searchQuery);
        InputStream fakeInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(fakeInput);
        this.userMenu = new UserMenu(defaultNumberOfOptions,this.data);
        Integer[] returnedIndexes = userMenu.findPerson();
        if (requiredResult != null) {
            Arrays.sort(requiredResult);
            Arrays.sort(returnedIndexes);
        }
        assertArrayEquals(returnedIndexes, requiredResult);
    }


    static Stream<Arguments> testHandleInput() {
        return Stream.of(
                Arguments.of(0,
                        "stop"
                ),
                Arguments.of(4,
                        null
                ),
                Arguments.of(2,
                        null
                )
        );
    }

    @ParameterizedTest(name = "{0}, {1}")
    @MethodSource("testHandleInput")
    void testHandleInputMethod(
            int option,
            String expectedOutput
    ){
        assertEquals(expectedOutput,userMenu.handleInput(option));
    }
}
