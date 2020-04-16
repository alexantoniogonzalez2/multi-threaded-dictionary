package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.Message;

import java.io.File;
import java.io.IOException;

class DictionaryTest {

    private Dictionary dictionary, editedDictionary, reOpenedDictionary;

    @BeforeEach
    void setUp() {
        // For tests 1 to 3
        dictionary = new Dictionary("dictionary");

        // For test 4
        editedDictionary = new Dictionary("dictionary");
        editedDictionary.addWord("one","number");
        editedDictionary.addWord("two","number");
        editedDictionary.addWord("three","number");
    }

    @Test // Test 1
    void addWord() {
        dictionary.removeWord("horse");
        dictionary.addWord("horse","animal");
        Assertions.assertTrue(dictionary.checkWord("horse"));
        String meaning = dictionary.getMeaning("horse");
        Assertions.assertEquals(meaning,"animal");
    }

    @Test // Test 2
    void removeWord() {
        dictionary.addWord("cat","animal");
        dictionary.removeWord("cat");
        Assertions.assertFalse(dictionary.checkWord("cat"));
    }

    @Test // Test 3
    void checkWord() {
        dictionary.removeWord("dog");
        String meaning = dictionary.getMeaning("dog");
        Assertions.assertEquals(meaning,null);
        Assertions.assertFalse(dictionary.checkWord("dog"));
    }

    @Test // Test 4
    void saveDictionary() {
        editedDictionary.saveDictionary();
        reOpenedDictionary = new Dictionary("dictionary");
        Assertions.assertEquals(editedDictionary.toString(),reOpenedDictionary.toString());
    }

    @Test // Test 1: query meaning of existing word.
    void processMessage1() {
        dictionary.addWord("banana", "fruit");
        Message query1 = new Message("query", "banana", "", "ENG");
        Message realOutput1 = dictionary.generateAnswer(query1);
        Message testOutput1 = new Message("meaning", "banana", "fruit", "ENG");
        Assertions.assertEquals(realOutput1.toString(), testOutput1.toString());
    }

    @Test // Test 2: query meaning of unknown word.
    void processMessage2() {
        Message query2 = new Message("query","ThisWordDoesNotExist","", "ENG");
        Message realOutput2 = dictionary.generateAnswer(query2);
        Message testOutput2 = new Message("unknown_word","ThisWordDoesNotExist","", "ENG");
        Assertions.assertEquals(realOutput2.toString(),testOutput2.toString());
    }

    @Test // Test 3: add unknown word.
    void processMessage3() {
        Message query3 = new Message("add","ThisWordDoesNotExist","fruit", "ENG");
        Message realOutput3 = dictionary.generateAnswer(query3);
        Message testOutput3 = new Message("added","ThisWordDoesNotExist","", "ENG");
        Assertions.assertEquals(realOutput3.toString(),testOutput3.toString());
    }

    @Test // Test 4: add already existing word.
    void processMessage4() {
        dictionary.addWord("apple","fruit");
        Message query4 = new Message("add","apple","fruit", "ENG");
        Message realOutput4 = dictionary.generateAnswer(query4);
        Message testOutput4 = new Message("word_already_exists","apple","", "ENG");
        Assertions.assertEquals(realOutput4.toString(),testOutput4.toString());
    }

    @Test // Test 5: remove unknown word.
    void processMessage5() {
        Message query5 = new Message("remove","AnotherUnknownWord","", "ENG");
        Message realOutput5 = dictionary.generateAnswer(query5);
        Message testOutput5 = new Message("word_does_not_exist","AnotherUnknownWord","", "ENG");
        Assertions.assertEquals(realOutput5.toString(),testOutput5.toString());
    }

    @Test // Test 6: remove existing word
    void processMessage6() {
        dictionary.addWord("banana","fruit");
        Message query6 = new Message("remove","banana","", "ENG");
        Message realOutput6 = dictionary.generateAnswer(query6);
        Message testOutput6 = new Message("removed","banana","", "ENG");
        Assertions.assertEquals(realOutput6.toString(),testOutput6.toString());
    }
}