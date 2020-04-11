
package server;

import java.util.HashMap;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

/**
 *
 * @author alex
 */
public class Dictionary {

    // Create a HashMap object called capitalCities
    // final
    private HashMap<String, String> words = new HashMap<String, String>();

    public Dictionary(String filename){

        try {
            File myObj = new File(filename + ".txt");
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    String[] line_splitted = line.split(":");
                    words.put(line_splitted[0], line_splitted[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void addWord(String word, String meaning){
        this.words.put(word, meaning);
    }

    public void removeWord(String word){
        this.words.remove(word);
    }

    public String getMeaning(String word){
        return this.words.get(word);
    }

    public boolean checkWord(String word){
        return this.words.containsKey(word);
    }

    /*
    public void print(){
        System.out.println(words);

    }*/


}
