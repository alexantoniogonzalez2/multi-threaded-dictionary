package client;

import utilities.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.ObjectOutputStream;

import java.io.IOException;

public class ClientGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel searchLabel;
    private JTextField queryField;
    private JLabel queryResult;
    private JButton queryButton;
    private JLabel addLabel;
    private JTextField addWordField;
    private JTextField addMeaningField;
    private JButton addButton;
    private JLabel addResult;
    private JLabel removeLabel;
    private JTextField removeField;
    private JButton removeButton;
    private JLabel removeResult;

    public ClientGUI(String title, ObjectOutputStream out){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(40,70);
        mainPanel.setPreferredSize(new Dimension(480, 360));
        this.setContentPane(mainPanel);
        this.pack();

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = queryField.getText().replace(":","");
                if (inputText.equals("")) {
                    queryResult.setText("Please enter a word for querying!");
                } else {
                    Message message = new Message("query", inputText, "");
                    sendMessage(message, out);
                }
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = addWordField.getText().replace(":","");
                String inputMeaning = addMeaningField.getText().replace(":","");
                if (inputText.equals("")){
                    addResult.setText("Please enter a word for adding!");
                } else if (inputMeaning.equals("")) {
                    addResult.setText("Please enter a meaning for the word!");
                } else {
                    Message message = new Message("add", inputText, inputMeaning);
                    sendMessage(message, out);
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = removeField.getText().replace(":","");
                if (inputText.equals("")){
                    removeResult.setText("Please enter a word for deleting!");
                } else {
                    Message message = new Message("remove", inputText,"");
                    sendMessage(message, out);
                }
            }
        });
    }

    private void sendMessage(Message message,ObjectOutputStream out){
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void processServerMessage(Message message){
        String type = message.getType();
        String word = message.getWord();
        String text = message.getText();

        switch(type){
            case "meaning":
                this.queryResult.setText("Meaning: " + text);
                break;
            case "unknown_word":
                queryResult.setText("Word not found in the dictionary!");
                break;
            case "added":
                addResult.setText("Word added to the dictionary: " + word);
                break;
            case "word_already_exists":
                addResult.setText("This word already exists in the dictionary!");
                break;
            case "removed":
                removeResult.setText("Word removed from the dictionary: " + word);
                break;
            case "word_does_not_exist":
                removeResult.setText("This word does not exist!");
                break;
            default:
                System.out.println("Unknown type message.");
                break;
        }
    }
}