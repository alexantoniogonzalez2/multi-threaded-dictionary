package utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.Dictionary;

class MessageTest {

    @Test
    void processMessage() {
        Dictionary dictionary = new Dictionary("dictionary");

        // Test 1: query meaning of existing word
        Message query_1 = new Message("query","banana","");
        Message answer_func_1 = query_1.ProcessMessage(dictionary);
        Message answer_test_1 = new Message("meaning","banana","fruit");
        Assertions.assertEquals(answer_func_1.toString(),answer_test_1.toString());

        // Test 2: query meaning of unknown word
        Message query_2 = new Message("query","ThisWordDoesNotExist","");
        Message answer_func_2 = query_2.ProcessMessage(dictionary);
        Message answer_test_2 = new Message("unknown_word","ThisWordDoesNotExist","");
        Assertions.assertEquals(answer_func_2.toString(),answer_test_2.toString());

        // Test 3: add unknown word
        Message query_3 = new Message("add","pineapple","fruit");
        Message answer_func_3 = query_3.ProcessMessage(dictionary);
        Message answer_test_3 = new Message("added","pineapple","");
        Assertions.assertEquals(answer_func_3.toString(),answer_test_3.toString());

        // Test 4: add existing word
        Message query_4 = new Message("add","apple","fruit");
        Message answer_func_4 = query_4.ProcessMessage(dictionary);
        Message answer_test_4 = new Message("word_already_exists","apple","");
        Assertions.assertEquals(answer_func_4.toString(),answer_test_4.toString());

        // Test 5: remove unknown word
        Message query_5 = new Message("remove","AnotherUnknownWord","");
        Message answer_func_5 = query_5.ProcessMessage(dictionary);
        Message answer_test_5 = new Message("word_does_not_exist","AnotherUnknownWord","");
        Assertions.assertEquals(answer_func_5.toString(),answer_test_5.toString());

        // Test 5: remove existing word
        Message query_6 = new Message("remove","banana","");
        Message answer_func_6 = query_6.ProcessMessage(dictionary);
        Message answer_test_6 = new Message("removed","banana","");
        Assertions.assertEquals(answer_func_6.toString(),answer_test_6.toString());
    }
}