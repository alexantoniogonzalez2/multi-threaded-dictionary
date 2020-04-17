
package utilities;

import java.io.Serializable;

/**
 *
 * @author alex
 */
// must implement Serializable in order to be sent
public class Message implements Serializable {
    private final String type;
    private final String word;
    private final String text;
    private final String language;

    public Message(String type, String word, String text, String language) {
        this.type = type;
        this.word = word;
        this.text = text;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }

    // Used for testing
    @Override
    public String toString() {
        return "Message{" +
                "type='" + type +
                ", word='" + word +
                ", text='" + text +
                ", language ='" + language +
                '}';
    }
}