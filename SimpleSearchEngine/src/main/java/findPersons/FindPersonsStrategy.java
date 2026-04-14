package findPersons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public interface FindPersonsStrategy {
    Integer[] findPersons(String inputQuery, Map<String, HashSet<Integer>> inverseIndex);
    default String[] splitInput(String inputString){
        return Arrays.stream(inputString.split(" "))
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }
}