
package utilities;
import server.Dictionary;

import java.io.Serializable;

/**
 *
 * @author alex
 */
// must implement Serializable in order to be sent
public class Message implements Serializable{
    private final String type;
    private final String word;
    private final String text;

    public Message(String type, String word, String text) {
        this.type = type;
        this.word = word;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getWord() {
        return word;
    }

    public String getText() {
        return text;
    }

    // Used for testing
    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", word='" + word + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public Message generateServerMessage(Dictionary dictionary){
        Message answer;
        Boolean word_exists;

        switch(type){
            case "query":
                String meaning = dictionary.getMeaning(word);
                if (meaning != null)
                    answer = new Message("meaning",word,meaning);
                else {
                    String similarWords = dictionary.getSimilarWords(word);
                    if (similarWords != "")
                        answer = new Message("similar_words",word,similarWords);
                    else
                        answer = new Message("unknown_word",word,"");
                }
                break;
            case "add":
                word_exists = dictionary.checkWord(word);
                if (word_exists) {
                    answer = new Message("word_already_exists", word, "");
                } else {
                    dictionary.addWord(word, text);
                    answer = new Message("added", word, "");
                }
                break;
            case "remove":
                word_exists = dictionary.checkWord(word);
                if (word_exists) {
                    dictionary.removeWord(word);
                    answer = new Message("removed", word, "");
                } else {
                    answer = new Message("word_does_not_exist", word, "");
                }
                break;
            default:
                answer = new Message("unknown_option", word, "");
        }
        return answer;
    }
}