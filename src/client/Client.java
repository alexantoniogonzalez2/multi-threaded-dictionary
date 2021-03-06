// Author: Alex Gonzalez Login ID: aagonzalez
// Purpose: Assignment 1 - COMP90015: Distributed Systems

package client;
import utilities.Message;
// Sockets libraries.
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
// Exception libraries.
import java.io.IOException;
import java.io.EOFException;
import java.net.ConnectException;
// GUI libraries.
import javax.swing.*;

// Server class represents a client and includes the manipulation of a socket.
public class Client {

    public static void main (String[] args) {

        // Reading the port number and the ip
        String ip = "";
        int port = 0;

        try {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException Exception ){
            errorMessage("Wrong Number of Parameters");
            System.exit(1);
        } catch (NumberFormatException Exception){
            errorMessage("Wrong Input Type for Port Number");
            System.exit(1);
        }

        // Try clause needed for socket object manipulation.
        try {
            Socket socket = new Socket(ip, port);
            System.out.println("Connection established");

            // Create objects for communicating with the client.
            // Detailed alternative: InputStream = clientSocket.getInputStream()
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // The first message of the server is expect to inform the available languages.
            String availableDict = availableDictionaries(in);
            // If an answer is received it is created the GUI for the user.
            ClientGUI clientGUI = new ClientGUI("Dictionary", out, availableDict);
            clientGUI.setVisible(true);

            // In the GUI will send the messages, the answers will be read here.
            Message serverMsg;
            try {
                while ((serverMsg = (Message) in.readObject()) != null )
                    clientGUI.processServerMessage(serverMsg);

            } catch (ClassNotFoundException exception) { // Exception throws by readObject method.
                errorMessage("Internal Error");
                System.exit(2);
            }
        } catch (ConnectException exception) {
            errorMessage("Unsuccessful Connection Attempt");
            System.exit(1);
        } catch (EOFException exception) {
            errorMessage("Server Disconnection");
            System.exit(1);
        } catch (IOException exception) { // Exception throws by socket object methods.
            errorMessage("Internal Error");
            System.exit(2);
        }
    }

    // Processing of the first message with the available languages
    private static String availableDictionaries (ObjectInputStream in) {
        Message serverResponse = null;
        try {
            serverResponse = (Message) in.readObject();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return serverResponse.getText();
    }

    // Method used for show error information to the user. With all of these cases the program is closed.
    protected static void errorMessage (String exception) {

        String errorMsg = "Error: " + exception + ". ";

        switch (exception) {
            case "Wrong Number of Parameters":
                errorMsg += "It was expected at least two arguments, port number (integer) " +
                        "and dictionary name (string).";
                break;
            case "Wrong Input Type for Port Number":
                errorMsg += "It was expected an integer number for the port argument.";
                break;
            case "Unsuccessful Connection Attempt":
                errorMsg += "Server connection refused, please check and try again.";
                break;
            case "Server Disconnection":
                errorMsg +="Server connection lost, please and check try again.";
                break;
            case "Internal Error":
                errorMsg +="The program will close.";
                break;
            default:
                System.out.println("No tracked error.");

        }
        JOptionPane.showMessageDialog(new JFrame(),errorMsg ,"Error", JOptionPane.ERROR_MESSAGE);
    }
}