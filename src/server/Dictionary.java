
package server;

import java.io.FileWriter;
import java.util.HashMap;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import java.io.IOException;


import org.apache.commons.text.similarity.LevenshteinDistance;


/**
 *
 * @author alex
 */
public class Dictionary {

    // Create a HashMap object for the words of dictionary
    private HashMap<String, String> words = new HashMap();


    //
    private String fileName;

    public Dictionary (String fileName) {

        this.fileName = fileName;
        File myObj = new File(fileName + ".txt");
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
        } catch (FileNotFoundException exception) {
            ErrorInfo("File Not Found");
            System.exit(1);
        }
    }

    public void addWord (String word, String meaning) {
        this.words.put(word, meaning);
    }

    public void removeWord (String word) {
        this.words.remove(word);
    }

    public String getMeaning (String word){
        return this.words.get(word);
    }

    public String getSimilarWords (String word){
        String similarWords = "";
        int count = 0;
        LevenshteinDistance distance = new LevenshteinDistance();

        for (HashMap.Entry<String, String> entry : words.entrySet()) {
            if (count == 3)
                break;
            if (distance.apply(entry.getKey(), word) < 3){
                similarWords += entry.getKey() + ", ";
                count++;
            }
        }

        if (similarWords != "")
            similarWords = similarWords.substring(0, similarWords.length()-2);

        return similarWords;
    }

    public boolean checkWord (String word) {
        return this.words.containsKey(word);
    }
    
    public void saveDictionary () {
        try {
            FileWriter writer = new FileWriter(fileName + ".txt");
            for (HashMap.Entry<String, String> entry : words.entrySet()) {
                String word = entry.getKey();
                String meaning = entry.getValue();
                writer.write(word + ":" + meaning + "\n");
            }
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    private static void ErrorInfo (String exception) {

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

    @Override
    public String toString() {
        return "Dictionary{" +
                "words=" + words +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}