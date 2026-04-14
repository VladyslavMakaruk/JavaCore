package findPersons;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static util.DataInitializer.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Stream;

@DisplayName("Finn persons ALL strategy test")
public class FindPersonsAllTest {
    private final FindPersonsAll findPersonStrategy = new FindPersonsAll();
    private final Map<String, HashSet<Integer>> invertedIndex = new HashMap<>();
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

    FindPersonsAllTest(){
        initializeInvertedIndex(this.data,this.invertedIndex);
    }

    static Stream<Arguments> testAllInput() {
        return Stream.of(
                Arguments.of(
                        "Roman USA",
                        new Integer[]{1}
                ),
                Arguments.of(
                        "Ukraine",
                        new Integer[]{0,5,6}
                ),
                Arguments.of(
                        "Roman",
                        new Integer[]{1,7}
                ),
                Arguments.of(
                        "test",
                        null
                )
        );
    }

    @ParameterizedTest(name = "search query: {0}, indexes: {1}")
    @MethodSource("testAllInput")
    void testAllStrategy(
            String searchQuery,
            Integer[] requiredResult
    ){
        Integer[] returnedIndexes = this.findPersonStrategy.findPersons(searchQuery,this.invertedIndex);
        if (requiredResult != null) {
            Arrays.sort(requiredResult);
            Arrays.sort(returnedIndexes);
        }
        assertArrayEquals(returnedIndexes, requiredResult);
    }
}

