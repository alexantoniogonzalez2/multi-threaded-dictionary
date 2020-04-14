
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

    // Create a HashMap object for the words of dictionary
    private HashMap<String, String> words = new HashMap();

    public Dictionary(String filename){

        File myObj = new File(filename + ".txt");
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                try {
                    String[] splitLine = line.split(":");
                    words.put(splitLine[0], splitLine[1]);
                } catch (Exception ArrayIndexOutOfBoundsException){
                    ErrorInfo("Problem With Format");
                    System.exit(1);
                }
            }
        } catch (FileNotFoundException e) {
            ErrorInfo("File Not Found");
            System.exit(1);
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

    private static void ErrorInfo(String exception){

        String output = "Error: " + exception + ". ";

        switch (exception){
            case "Problem With Format":
                output += "There was a problem reading the file. Wrong word:meaning format.";
                break;
            case "File Not Found":
                output += "It was not found the file 'file_name.txt' in the directory.";
                break;
            default:
                output += "No tracked error.";
        }
        System.out.println(output);
    }
}