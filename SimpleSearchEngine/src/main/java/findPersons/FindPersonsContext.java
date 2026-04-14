package findPersons;

import java.util.HashSet;
import java.util.Map;

public class FindPersonsContext {
    FindPersonsStrategy strategy;
    public FindPersonsContext(FindPersonsStrategy strategy){
        this.strategy = strategy;
    }
    public Integer[] findPersons(String inputQuery, Map<String, HashSet<Integer>> inverseIndex) {
        return  this.strategy.findPersons(inputQuery,inverseIndex);
    };
}
