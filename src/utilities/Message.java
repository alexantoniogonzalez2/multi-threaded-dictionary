/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;
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

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", word='" + word + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

