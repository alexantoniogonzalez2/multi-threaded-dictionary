package client;
import utilities.Message;

import java.awt.*;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.Border;


public class Client {

    public static void main(String[] args){

        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        JFrame initialFrame = getInitialFrame();
        initialFrame.setVisible(true);

        try (Socket socket = new Socket(ip, port)){

            System.out.println("Connection established");

            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            ClientGUI clientGUI = new ClientGUI("Dictionary", out);
            initialFrame.setVisible(false);
            clientGUI.setVisible(true);

            Message serverMsg;
            try {
                while ((serverMsg = (Message) in.readObject()) != null)
                    clientGUI.processServerMessage(serverMsg);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (ConnectException Exception) { // Unsuccessful connection attempt
            initialFrame.getContentPane().removeAll();
            initialFrame.add(getJLabel("Server connection refused. Please check and try again."));
            Exception.printStackTrace();
        } catch (EOFException Exception){ // Server disconnection
            initialFrame.getContentPane().removeAll();
            initialFrame.add(getJLabel("Server connection lost. Please check try again."));
            initialFrame.setVisible(true);
            Exception.printStackTrace();
        }
        catch(UnknownHostException Exception) {
            Exception.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static JFrame getInitialFrame(){
        JFrame initialFrame = new JFrame();
        initialFrame.setLocation(40,70);
        initialFrame.setTitle("Dictionary");

        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(480, 360));

        // border
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        contentPanel.setBorder(padding);

        // Text
        contentPanel.add(getJLabel("Connecting to server..."));

        //JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        //separator.setMaximumSize( new Dimension(Integer.MAX_VALUE, 1) );
        //contentPanel.add(separator);

        initialFrame.setContentPane(contentPanel);
        initialFrame.pack();

        return initialFrame;
    }

    private static JLabel getJLabel(String text) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setFont(new Font("", Font.PLAIN, 18));

        return label;
    }
}