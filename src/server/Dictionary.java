// Author: Alex Gonzalez Login ID: aagonzalez
// Purpose: Assignment 1 - COMP90015: Distributed Systems

package server;
import utilities.Message;
// Files managing libraries.
import java.io.FileWriter;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
// Exception libraries.
import java.io.IOException;
import java.io.FileNotFoundException;
// Library used to suggest similar words.
import org.apache.commons.text.similarity.LevenshteinDistance;

public class Dictionary {

    // Attributes
    private final String fileName;
    private String language;
    // Create a HashMap object for the words of dictionary.
    private HashMap<String, String> words = new HashMap();

    public Dictionary (String fileName) {

        this.fileName = fileName;
        File myObj = new File(fileName + ".txt");
        try (Scanner myReader = new Scanner(myObj)) {

            // First line for language.
            String languageLine = myReader.nextLine();
            this.language = languageLine.replaceAll("\\s+","");

            // Reading words...
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                try {
                    String[] splitLine = line.split(":");
                    words.put(splitLine[0], splitLine[1]);
                } catch (Exception ArrayIndexOutOfBoundsException){
                    errorInfo("Problem With Format");
                    System.exit(1);
                }
            }
        } catch (FileNotFoundException exception) {
            errorInfo("File Not Found");
            System.exit(1);
        }
    }

    public String getLanguage() {
        return language;
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

    public boolean checkWord (String word) {
        return this.words.containsKey(word);
    }

    // This method generates the proper message required as answer
    public Message generateAnswer (Message message) {

        Message answer;
        Boolean wordExists;
        String type = message.getType();
        String word = message.getWord();
        String text = message.getText();
        String language = message.getLanguage();

        switch(type){
            case "query":
                String meaning = this.getMeaning(word);
                if (meaning != null)
                    answer = new Message("meaning",word,meaning,language);
                else {
                    // If the word is not found, similar words are searched.
                    String similarWords = this.getSimilarWords(word);
                    if (!similarWords.equals(""))
                        answer = new Message("similar_words",word,similarWords,language);
                    else
                        answer = new Message("unknown_word",word,"",language);
                }
                break;
            case "add":
                wordExists = this.checkWord(word);
                if (wordExists) {
                    answer = new Message("word_already_exists", word, "",language);
                } else {
                    this.addWord(word, text);
                    answer = new Message("added", word, "",language);
                }
                break;
            case "remove":
                wordExists = this.checkWord(word);
                if (wordExists) {
                    this.removeWord(word);
                    answer = new Message("removed", word, "",language);
                } else {
                    answer = new Message("word_does_not_exist", word, "",language);
                }
                break;
            default:
                answer = new Message("unknown_option", word, "",language);
        }
        return answer;
    }

    public String getSimilarWords (String word){

        String similarWords = "";
        int count = 0;
        LevenshteinDistance distance = new LevenshteinDistance();

        // It is review all the dictionary (up to three words).
        // As an improvement, this option could be optional.
        for (HashMap.Entry<String, String> entry : words.entrySet()) {
            if (count == 3)
                break;
            // The words with distance 1 or two are selected.
            if (distance.apply(entry.getKey(), word) < 3){
                similarWords += entry.getKey() + ", ";
                count++;
            }
        }

        // Just a final string processing.
        if (!similarWords.equals(""))
            similarWords = similarWords.substring(0, similarWords.length()-2);

        return similarWords;
    }

    // This method is used for proper error information.
    private static void errorInfo (String exception) {

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
    // Method for saving the dictionary with the same filename.
    public void saveDictionary () {
        try {
            FileWriter writer = new FileWriter(fileName + ".txt");

            // First line for language.
            writer.write(this.language + "\n");

            // Saving each word line by line
            for (HashMap.Entry<String, String> entry : words.entrySet()) {
                String word = entry.getKey();
                String meaning = entry.getValue();
                writer.write(word + ":" + meaning + "\n");
            }
            writer.close();
        } catch (IOException exception) { // Exception throws by FileWriter.write method.
            exception.printStackTrace();
            System.exit(2);
        }
    }

    // Used for testing.
    @Override
    public String toString() {
        return "Dictionary{" +
                "words=" + words +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}