package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}