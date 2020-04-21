// Author: Alex Gonzalez Login ID: aagonzalez
// Purpose: Assignment 1 - COMP90015: Distributed Systems

package server;
import utilities.Message;
// Testing libraries.
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DictionaryTest {

    private Dictionary dictionary, editedDictionary, reOpenedDictionary;

    @BeforeEach
    void setUp() {
        // For tests 1 to 3
        dictionary = new Dictionary("test_dictionary");

        // For test 4
        editedDictionary = new Dictionary("test_dictionary");
        editedDictionary.addWord("One","Number");
        editedDictionary.addWord("Two","Number");
        editedDictionary.addWord("Three","Number");
    }

    @Test // Test 1: add method
    void addWord() {
        dictionary.removeWord("Horse");
        dictionary.addWord("Horse","Animal");
        Assertions.assertTrue(dictionary.checkWord("Horse"));
        String meaning = dictionary.getMeaning("Horse");
        Assertions.assertEquals(meaning,"Animal");
    }

    @Test // Test 2: remove method
    void removeWord() {
        dictionary.addWord("Cat","Animal");
        dictionary.removeWord("Cat");
        Assertions.assertFalse(dictionary.checkWord("Cat"));
    }

    @Test // Test 3: checkWord method
    void checkWord() {
        dictionary.removeWord("Dog");
        String meaning = dictionary.getMeaning("Dog");
        Assertions.assertEquals(meaning,null);
        Assertions.assertFalse(dictionary.checkWord("Dog"));
    }

    @Test // Test 4: saveDictionary method
    void saveDictionary() {
        editedDictionary.saveDictionary();
        reOpenedDictionary = new Dictionary("test_dictionary");
        Assertions.assertEquals(editedDictionary.toString(),reOpenedDictionary.toString());
    }

    @Test // Test 5: query meaning of existing word.
    void processMessage1() {
        dictionary.addWord("Banana", "Fruit");
        Message query1 = new Message("query", "Banana", "", "ENG");
        Message realOutput1 = dictionary.generateAnswer(query1);
        Message testOutput1 = new Message("meaning", "Banana", "Fruit", "ENG");
        Assertions.assertEquals(realOutput1.toString(), testOutput1.toString());
    }

    @Test // Test 6: query meaning of unknown word.
    void processMessage2() {
        Message query2 = new Message("query","ThisWordDoesNotExist","", "ENG");
        Message realOutput2 = dictionary.generateAnswer(query2);
        Message testOutput2 = new Message("unknown_word","ThisWordDoesNotExist","", "ENG");
        Assertions.assertEquals(realOutput2.toString(),testOutput2.toString());
    }

    @Test // Test 7: add unknown word.
    void processMessage3() {
        Message query3 = new Message("add","ThisWordDoesNotExist","Fruit", "ENG");
        Message realOutput3 = dictionary.generateAnswer(query3);
        Message testOutput3 = new Message("added","ThisWordDoesNotExist","", "ENG");
        Assertions.assertEquals(realOutput3.toString(),testOutput3.toString());
    }

    @Test // Test 8: add already existing word.
    void processMessage4() {
        dictionary.addWord("Apple","Fruit");
        Message query4 = new Message("add","Apple","Fruit", "ENG");
        Message realOutput4 = dictionary.generateAnswer(query4);
        Message testOutput4 = new Message("word_already_exists","Apple","", "ENG");
        Assertions.assertEquals(realOutput4.toString(),testOutput4.toString());
    }

    @Test // Test 9: remove unknown word.
    void processMessage5() {
        Message query5 = new Message("remove","AnotherUnknownWord","", "ENG");
        Message realOutput5 = dictionary.generateAnswer(query5);
        Message testOutput5 = new Message("word_does_not_exist","AnotherUnknownWord","", "ENG");
        Assertions.assertEquals(realOutput5.toString(),testOutput5.toString());
    }

    @Test // Test 10: remove existing word
    void processMessage6() {
        dictionary.addWord("Banana","Bruit");
        Message query6 = new Message("remove","Banana","", "ENG");
        Message realOutput6 = dictionary.generateAnswer(query6);
        Message testOutput6 = new Message("removed","Banana","", "ENG");
        Assertions.assertEquals(realOutput6.toString(),testOutput6.toString());
    }
}