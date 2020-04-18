// Author: Alex Gonzalez Login ID: aagonzalez
// Purpose: Assignment 1 - COMP90015: Distributed Systems

package client;
import utilities.Message;
// GUI libraries.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
// Socket and exception libraries.
import java.io.ObjectOutputStream;
import java.io.IOException;

// This class is responsible for the GUI.
public class ClientGUI extends JFrame {
    // Attributes utilized for design a view.
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

        // 'dictionaries' is the radio button group for show the available languages.
        // Each radio button is added to the radio button group.
        this.dictionaries.add(this.radioButton1);
        this.dictionaries.add(this.radioButton2);
        this.dictionaries.add(this.radioButton3);
        this.dictionaries.add(this.radioButton4);
        this.dictionaries.add(this.radioButton5);

        // String processing to obtain the available languages.
        String[] languages = (availableDict.substring(1,availableDict.length()-1)).replaceAll("\\s","").split(",");
        int max = languages.length;
        int count = 0;
        for (Enumeration<AbstractButton> buttons = dictionaries.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            // Each available language is assigned to a radio button.
            if (count < max) {
                button.setText(languages[count]);
                button.setActionCommand(languages[count]);
            }
            else { // The remaining radio buttons are hidden.
                button.setVisible(false);
            }
            count++;
        }

        // General set up for the panel, main content for the frame.
        mainPanel.setPreferredSize(new Dimension(620, 530));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(40,70);
        this.setContentPane(mainPanel);
        this.pack();

        // Only these characters are allowed.
        String allowed = "[a-zA-Z0-9áéíóúàèìòùäëïöü., ]+";

        // Event listener for the 'query' feature.
        class queryActionListener implements ActionListener {
            // The actions performed include checking that inputs are correct.
            // A message will be send if everything is correct.
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
        // The listener is added to the corresponding elements.
        queryActionListener queryListener = new queryActionListener();
        queryField.addActionListener(queryListener);
        queryButton.addActionListener(queryListener);

        // Event listener for the 'add' feature.
        class AddWordActionListener implements ActionListener {
            // The actions performed include checking that inputs are correct.
            // A message will be send if everything is correct.
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
        // The listener is added to the corresponding elements.
        AddWordActionListener addListener = new AddWordActionListener();
        addButton.addActionListener(addListener);
        addWordField.addActionListener(addListener);
        addMeaningField.addActionListener(addListener);

        // Event listener for the 'remove' feature.
        class RemoveWordActionListener implements ActionListener {
            // The actions performed include checking that inputs are correct.
            // A message will be send if everything is correct.
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
        // The listener is added to the corresponding elements.
        RemoveWordActionListener RemoveListener = new RemoveWordActionListener();
        removeButton.addActionListener(RemoveListener);
        removeField.addActionListener(RemoveListener);
    }

    // This method is used to send a message to the server.
    private void sendMessage (ObjectOutputStream out, String type, String word, String text, String language) {

        // The word is 'normalized' capital format.
        String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        Message message = new Message(type, capitalizedWord, text, language);
        // The message is sent.
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // This method is used for proper error information.
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
        // It is launched a message dialog (kind of pop-up).
        JOptionPane.showMessageDialog(new JFrame(), text, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    // This method process the answer from the server.
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