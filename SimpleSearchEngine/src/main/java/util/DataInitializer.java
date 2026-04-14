package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DataInitializer {

    public static void initializeInvertedIndex(List<String> data, Map<String, HashSet<Integer>> invertedIndex){
        for(int i = 0; i<data.size();i++){
            String dataLine = data.get(i);
            String[] wordsOfLine =  dataLine.split(" ");
            for(String word: wordsOfLine){
                word = word.toLowerCase();
                if (invertedIndex.containsKey(word)){
                    invertedIndex.get(word).add(i);
                } else {
                    HashSet<Integer> newSet = new HashSet<>();
                    newSet.add(i);
                    invertedIndex.put(word, newSet);
                }
            }
        }
    }


    public static void readDataFromFile(List<String>data, String filename) throws FileNotFoundException {
        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                data.addLast(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + "e");
            throw e;
        }
    }
}
