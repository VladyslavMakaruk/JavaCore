package findPersons;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static util.DataInitializer.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Stream;

@DisplayName("Finn persons NONE strategy test")
public class FindPersonsNoneTest {
    private final FindPersonsNone findPersonStrategy;
    private final Map<String, HashSet<Integer>> invertedIndex = new HashMap<>();
    private final ArrayList<String> data = new ArrayList<>(List.of(
            "Andrew Ukraine Kyiv 000",          //0
            "Roman USA New Jersey 001",         //1
            "Mark USA Phoenix 010",             //2
            "Julia Spain Madrid 011",           //3
            "Linus Finland Helsinki 100",       //4
            "Vladyslav Ukraine Ternopil 101",   //5
            "Kateryna Ukraine Rivne 110",       //6
            "Roman Poland Wroclaw 111"          //7
    ));

    FindPersonsNoneTest(){
        initializeInvertedIndex(this.data,this.invertedIndex);
        findPersonStrategy = new FindPersonsNone(this.data.size());
    }

    static Stream<Arguments> testNoneInput() {
        return Stream.of(
                Arguments.of(
                        "Roman Ukraine",
                        new Integer[]{2,3,4}
                ),
                Arguments.of(
                        "Roman",
                        new Integer[]{0,2,3,4,5,6}
                ),
                Arguments.of(
                        "Ukraine USA Madrid 100",
                        new Integer[]{7}
                ),
                Arguments.of(
                        "Ukraine USA Madrid 100 111",
                        new Integer[]{}
                ),
                Arguments.of(
                        "Valeriy",
                        new Integer[]{0,1,2,3,4,5,6,7}
                )
        );
    }

    @ParameterizedTest(name = "search query: {0}, indexes: {1}")
    @MethodSource("testNoneInput")
    void testNoneStrategy(
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

