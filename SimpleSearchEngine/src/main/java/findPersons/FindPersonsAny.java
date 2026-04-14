package findPersons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindPersonsAny implements FindPersonsStrategy{
    @Override
    public Integer[] findPersons(String searchQuery, Map<String, HashSet<Integer>> inverseIndex) {
        String[] wordsInQuery = this.splitInput(searchQuery);
        ArrayList<Set<Integer>> listOfIndexes = new ArrayList<>();
        for (String wordInQuery : wordsInQuery){
            if (inverseIndex.containsKey(wordInQuery)) {
                Set<Integer> wordIndexes = inverseIndex.get(wordInQuery);
                listOfIndexes.add(wordIndexes);
            }
        }
        Set<Integer> initSet = listOfIndexes.isEmpty() ? new HashSet<>() : listOfIndexes.removeFirst();
        for (Set<Integer> indexes : listOfIndexes){
            initSet.addAll(indexes);
        }
        return initSet.isEmpty() ? null : initSet.toArray(new Integer[0]);
    }

}
