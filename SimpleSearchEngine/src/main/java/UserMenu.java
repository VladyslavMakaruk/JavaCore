import findPersons.*;


import java.io.FileNotFoundException;
import java.util.*;
import static util.DataInitializer.*;

public class UserMenu {
    private List<String> data = new ArrayList<>();
    private final Map<String,HashSet<Integer>> invertedIndex = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private final int numberOfOptions;
    private final String stopCondition = "stop";

    UserMenu(int numberOfOptions, String filename) throws FileNotFoundException {
        this.numberOfOptions = numberOfOptions;
        readDataFromFile(data,filename);
        initializeInvertedIndex(data,invertedIndex);
    }

    UserMenu(int numberOfOptions, List<String> data) throws FileNotFoundException {
        this.numberOfOptions = numberOfOptions;
        this.data = data;
        initializeInvertedIndex(data, invertedIndex);
    }

    public void Start(){
        while (true) {
            printMenu();
            scanner.nextLine();
            String condition = handleInput(scanner.nextInt());
            if (condition != null){
                break;
            }
        }
    }

    public void printMenu(){
        System.out.println(
                "=== Menu ===\n" +
                        "1. Find a person\n" +
                        "2. Print all people\n" +
                        "0. Exit"
        );
    }

    String handleInput(int option){
        if (option < 0 || option > this.numberOfOptions-1) {
            System.out.println("Incorrect option! Try again.");
        } else {
            switch (option) {
                case 0:
                    return stopCondition;
                case 1:
                    printData(findPerson());
                    break;
                case 2:
                    printAllPeople();
                    break;
            }
        }
        return null;
    }

    private void printAllPeople(){
        System.out.println("=== List of people ===");
        for(String userData: data){
            System.out.println(userData);
        }
    }

    private void printData(Integer[] indexes){
        if (indexes == null || indexes.length == 0){
            System.out.println("No matching people found.");
            return;
        }
        for (int index: indexes){
            System.out.println(this.data.get(index));
        }
    }

    Integer[] findPerson(){
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String matchingStrategy = scanner.nextLine();
        FindPersonsStrategy strategy = switch(matchingStrategy) {
            case "ALL" -> new FindPersonsAll();
            case "ANY" -> new FindPersonsAny();
            case "NONE" -> new FindPersonsNone(this.data.size());
            default -> throw new RuntimeException("There is not " + matchingStrategy + " type of search strategy");
        };

        FindPersonsContext searchContext = new FindPersonsContext(strategy);
        System.out.println("Enter a name or email to search all suitable people.");
        String valueToSearch = scanner.nextLine().toLowerCase();
        Integer[] dataIndexes = searchContext.findPersons(valueToSearch,this.invertedIndex);
        if (dataIndexes != null){
            Arrays.sort(dataIndexes);
        }
        return dataIndexes;
    }
}
