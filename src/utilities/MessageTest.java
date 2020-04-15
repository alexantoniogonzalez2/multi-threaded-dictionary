package utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Dictionary;

class MessageTest {

    private Dictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new Dictionary("dictionary");
    }

    @Test // Test 1: query meaning of existing word.
    void processMessage1() {
        dictionary.addWord("banana", "fruit");
        Message query1 = new Message("query", "banana", "");
        Message realOutput1 = query1.generateServerMessage(dictionary);
        Message testOutput1 = new Message("meaning", "banana", "fruit");
        Assertions.assertEquals(realOutput1.toString(), testOutput1.toString());
    }

    @Test // Test 2: query meaning of unknown word.
    void processMessage2() {
        Message query2 = new Message("query","ThisWordDoesNotExist","");
        Message realOutput2 = query2.generateServerMessage(dictionary);
        Message testOutput2 = new Message("unknown_word","ThisWordDoesNotExist","");
        Assertions.assertEquals(realOutput2.toString(),testOutput2.toString());
    }

    @Test // Test 3: add unknown word.
    void processMessage3() {
        Message query3 = new Message("add","ThisWordDoesNotExist","fruit");
        Message realOutput3 = query3.generateServerMessage(dictionary);
        Message testOutput3 = new Message("added","ThisWordDoesNotExist","");
        Assertions.assertEquals(realOutput3.toString(),testOutput3.toString());
    }

    @Test // Test 4: add already existing word.
    void processMessage4() {
        dictionary.addWord("apple","fruit");
        Message query4 = new Message("add","apple","fruit");
        Message realOutput4 = query4.generateServerMessage(dictionary);
        Message testOutput4 = new Message("word_already_exists","apple","");
        Assertions.assertEquals(realOutput4.toString(),testOutput4.toString());
    }

    @Test // Test 5: remove unknown word.
    void processMessage5() {
        Message query5 = new Message("remove","AnotherUnknownWord","");
        Message realOutput5 = query5.generateServerMessage(dictionary);
        Message testOutput5 = new Message("word_does_not_exist","AnotherUnknownWord","");
        Assertions.assertEquals(realOutput5.toString(),testOutput5.toString());
    }

    @Test // Test 6: remove existing word
    void processMessage6() {
        dictionary.addWord("banana","fruit");
        Message query6 = new Message("remove","banana","");
        Message realOutput6 = query6.generateServerMessage(dictionary);
        Message testOutput6 = new Message("removed","banana","");
        Assertions.assertEquals(realOutput6.toString(),testOutput6.toString());
    }
}