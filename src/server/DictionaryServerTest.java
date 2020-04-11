package server;

import org.junit.jupiter.api.Assertions;
import utilities.Message;

class DictionaryServerTest {

    @org.junit.jupiter.api.Test
    public void testProcessMessage(){

        Dictionary dictionary = new Dictionary("dictionary");

        // Test 1: query meaning of existing word
        Message query_1 = new Message("query","banana","");
        Message answer_func_1 = DictionaryServer.ProcessMessage(query_1,dictionary);
        Message answer_test_1 = new Message("meaning","banana","fruit");
        Assertions.assertEquals(answer_func_1.toString(),answer_test_1.toString());

        // Test 2: query meaning of unknown word
        Message query_2 = new Message("query","ThisWordDoesNotExist","");
        Message answer_func_2 = DictionaryServer.ProcessMessage(query_2,dictionary);
        Message answer_test_2 = new Message("unknown_word","ThisWordDoesNotExist","");
        Assertions.assertEquals(answer_func_2.toString(),answer_test_2.toString());

        // Test 3: add unknown word
        Message query_3 = new Message("add","pineapple","fruit");
        Message answer_func_3 = DictionaryServer.ProcessMessage(query_3,dictionary);
        Message answer_test_3 = new Message("added","pineapple","");
        Assertions.assertEquals(answer_func_3.toString(),answer_test_3.toString());

        // Test 4: add existing word
        Message query_4 = new Message("add","apple","fruit");
        Message answer_func_4 = DictionaryServer.ProcessMessage(query_4,dictionary);
        Message answer_test_4 = new Message("word_already_exists","apple","");
        Assertions.assertEquals(answer_func_4.toString(),answer_test_4.toString());

        // Test 5: remove unknown word
        Message query_5 = new Message("remove","AnotherUnknownWord","");
        Message answer_func_5 = DictionaryServer.ProcessMessage(query_5,dictionary);
        Message answer_test_5 = new Message("word_does_not_exist","AnotherUnknownWord","");
        Assertions.assertEquals(answer_func_5.toString(),answer_test_5.toString());

        // Test 5: remove existing word
        Message query_6 = new Message("remove","banana","");
        Message answer_func_6 = DictionaryServer.ProcessMessage(query_6,dictionary);
        Message answer_test_6 = new Message("removed","banana","");
        Assertions.assertEquals(answer_func_6.toString(),answer_test_6.toString());
    }
}