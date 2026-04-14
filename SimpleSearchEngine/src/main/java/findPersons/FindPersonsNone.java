package findPersons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class FindPersonsNone implements FindPersonsStrategy{

    int lenOfCollectedData;

    public FindPersonsNone(int lenOfCollectedData){
        this.lenOfCollectedData = lenOfCollectedData;
    }

    @Override
    public Integer[] findPersons(String searchQuery, Map<String, HashSet<Integer>> inverseIndex) {
        String[] wordsInQuery = this.splitInput(searchQuery);
        ArrayList<Set<Integer>> listOfIndexes = new ArrayList<>();
        for (String wordInQuery : wordsInQuery){
            if (inverseIndex.containsKey(wordInQuery)) {
                listOfIndexes.add(inverseIndex.get(wordInQuery));
            }
        }
        Set<Integer> initSet;
        if (!listOfIndexes.isEmpty()) {
            initSet = listOfIndexes.removeFirst();
        } else {
            initSet = new HashSet<>();
        }
        for (Set<Integer> indexes : listOfIndexes){
            initSet.addAll(indexes);
        }
        return initSet.isEmpty() ? IntStream.range(0, this.lenOfCollectedData)
                                            .boxed()
                                            .toArray(Integer[]::new)
                                 :
                                   IntStream.range(0, this.lenOfCollectedData)
                                            .boxed()
                                            .filter(i -> !initSet.contains(i))
                                            .toArray(Integer[]::new);
    }

}
