package client;

import utilities.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.ObjectOutputStream;

import java.io.IOException;
import java.util.Enumeration;

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
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JRadioButton radioButton5;
    private ButtonGroup dictionaries = new ButtonGroup();
    private JLabel dictionariesLabel;
    private JLabel dictionariesMessage;

    public ClientGUI (String title, ObjectOutputStream out, String availableDict) {

        super(title);

        // Adding dictionaries dynamically
        this.dictionaries.add(this.radioButton1);
        this.dictionaries.add(this.radioButton2);
        this.dictionaries.add(this.radioButton3);
        this.dictionaries.add(this.radioButton4);
        this.dictionaries.add(this.radioButton5);

        String[] languages = (availableDict.substring(1,availableDict.length()-1)).replaceAll("\\s","").split(",");
        int max = languages.length;
        int count = 0;
        for (Enumeration<AbstractButton> buttons = dictionaries.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (count < max) {
                button.setText(languages[count]);
                button.setActionCommand(languages[count]);
            }
            else {
                button.setVisible(false);
            }
            count++;
        }

        mainPanel.setPreferredSize(new Dimension(620, 530));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(40,70);
        this.setContentPane(mainPanel);
        this.pack();

        String allowed = "[a-zA-Z0-9áéíóúàèìòùäëïöü. ]+";

        class queryActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = queryField.getText();
                String selectedLng = dictionaries.getSelection().getActionCommand();
                boolean check = inputText.matches(allowed);

                if (inputText.equals(""))
                    showWarning("empty_word");
                else if (!check)
                    showWarning("restricted_character");
                else
                    sendMessage(out,"query", inputText, "", selectedLng);

            }
        }
        queryActionListener queryListener = new queryActionListener();
        queryField.addActionListener(queryListener);
        queryButton.addActionListener(queryListener);

        class AddWordActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = addWordField.getText();
                String inputMeaning = addMeaningField.getText();
                String selectedLng = dictionaries.getSelection().getActionCommand();
                boolean checkWord = inputText.matches(allowed);
                boolean checkMeaning = inputMeaning.matches(allowed);

                if (inputText.equals(""))
                    showWarning("empty_word");
                else if (!checkWord)
                    showWarning("restricted_character");
                else if (inputMeaning.equals(""))
                    showWarning("empty_meaning");
                else if (!checkMeaning)
                    showWarning("restricted_character");
                else
                    sendMessage(out, "add", inputText, inputMeaning, selectedLng);

            }
        }
        AddWordActionListener addListener = new AddWordActionListener();
        addButton.addActionListener(addListener);
        addWordField.addActionListener(addListener);
        addMeaningField.addActionListener(addListener);

        class RemoveWordActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = removeField.getText();
                String selectedLng = dictionaries.getSelection().getActionCommand();
                boolean check = inputText.matches(allowed);

                if (inputText.equals(""))
                    showWarning("empty_word");
                if (!check)
                    showWarning("restricted_character");
                else
                    sendMessage(out, "remove", inputText, "", selectedLng);
            }
        }
        RemoveWordActionListener RemoveListener = new RemoveWordActionListener();
        removeButton.addActionListener(RemoveListener);
        removeField.addActionListener(RemoveListener);
    }

    private void sendMessage (ObjectOutputStream out, String type, String word, String text, String language) {
        String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        Message message = new Message(type, capitalizedWord, text, language);
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void showWarning (String type) {
        String text = "";
        switch (type) {
            case "empty_word":
                text = "Please insert a word!";
                break;
            case "empty_meaning":
                text = "Please insert a meaning!";
                break;
            case "restricted_character":
                text = "You have inserted restricted characters. Only alphanumeric characters are allowed.";
                break;
            default:
        }
        JOptionPane.showMessageDialog(new JFrame(), text, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public void processServerMessage (Message message) {
        String type = message.getType();
        String word = message.getWord();
        String text = message.getText();


        switch(type){
            case "meaning":
                this.queryResult.setText("<html>Meaning: " + text+"<html>");
                break;
            case "similar_words":
                queryResult.setText("Word not found. Similar words: " + text);
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