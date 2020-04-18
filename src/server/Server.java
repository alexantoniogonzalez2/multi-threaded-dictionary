// Author: Alex Gonzalez Login ID: aagonzalez
// Purpose: Assignment 1 - COMP90015: Distributed Systems

package server;
import utilities.Message;
// Sockets libraries.
import java.net.Socket;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
// Exception libraries.
import java.io.IOException;
import java.io.EOFException;
import java.net.BindException;
import java.util.HashMap;

// Server class represents the server and includes the main manipulation of sockets and threads.
public class Server {

    // Maximum number of dictionaries
    private static final int dictLimit = 5;
    // Identifies the users connected
    private static int clients = 0;

    public static void main(String[] args) {
        
        // Reading the port number and the filenames
        int port = getPort(args);
        HashMap<String, Dictionary> dictionaries = getDictionaries(args);

        // Socket factory for creating sockets
        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try (ServerSocket server = factory.createServerSocket(port)){
            System.out.println("Waiting for client connections...");

            // Listen for connections...
            while (true){
                Socket client = server.accept();
                clients++;
                System.out.println("New client. Nº: " + clients);

                // It started a new thread for each connection.
                Thread thread = new Thread(() -> serveClient(client, dictionaries));
                thread.start();
            }
        } catch (IllegalArgumentException exception){
            errorInfo("Wrong Port Number");
            System.exit(1);
        } catch (BindException exception) {
            errorInfo("Port Already in Use");
            System.exit(1);
        } catch (IOException exception) { // Exception throws by createServerSocket method.
            exception.printStackTrace();
            System.exit(2);
        }
    }

    // This method represents a Thread instance for managing a socket communication with a client.
    private static void serveClient(Socket clientSocket, HashMap<String,Dictionary> dictionaries){

        // Try clause needed for socket object manipulation.
        try {
            // Create objects for communicating with the client.
            // Detailed alternative: InputStream = clientSocket.getInputStream()
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            // First message with the available dictionaries
            String availableDict = dictionaries.keySet().toString();
            Message firstMessage = new Message("available_dict","",availableDict,"");
            out.writeObject(firstMessage);
            out.flush();

            // Continuously reading and answering messages from the clients...
            Message clientMessage, serverAnswer;
            try {
                while ((clientMessage = (Message)in.readObject()) != null){

                    // It is identified the language.
                    String messageLanguage = clientMessage.getLanguage();
                    // It is asked the answer to the proper dictionary.
                    serverAnswer = (dictionaries.get(messageLanguage)).generateAnswer(clientMessage);
                    System.out.println("Message from client " + clients);

                    out.writeObject(serverAnswer);
                    out.flush();
                    System.out.println("Response sent.");
                }
            } catch (ClassNotFoundException exception){ // Exception throws by readObject method.
                exception.printStackTrace();
            }
        } catch (EOFException exception) {
            errorInfo("Client Closes Connection");
            // All the dictionaries are saved if a client closes the connection.
            dictionaries.forEach((String language, Dictionary dictionary) -> {
                dictionary.saveDictionary();
            });
            // The program continues...
        } catch (IOException exception) { // Exception throws by socket object methods.
            exception.printStackTrace();
            // The program continues...
        }
    }

    // Method for reading the port from the arguments.
    private static int getPort (String[] args){
        int port = 0;

        try {
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException Exception) {
            errorInfo("Wrong Number of Parameters");
            System.exit(1);
        } catch (NumberFormatException Exception) {
            errorInfo("Wrong Input Type for Port Number");
            System.exit(1);
        }
        return port;
    }

    // Method for reading the filenames from the arguments.
    private static HashMap<String, Dictionary> getDictionaries (String[] args) {
        String[] fileName = new String[dictLimit];

        try {
            fileName[0] = args[1];
        } catch (ArrayIndexOutOfBoundsException Exception) {
            errorInfo("Wrong Number of Parameters");
            System.exit(1);
        }

        try {
            for (int i = 1; i< dictLimit; i++)
                fileName[i] = args[i+1];
        } catch (ArrayIndexOutOfBoundsException exception) { // If there is less than 5 filenames
            // The program continues...
        }

        // Creating the dictionary objects.
        HashMap<String, Dictionary> dictionaries = new HashMap();
        for (int i = 0; i<5 ; i++)
            if (fileName[i] != null){
                Dictionary dictionary = new Dictionary(fileName[i]);
                dictionaries.put(dictionary.getLanguage(), dictionary);
                // Hook for save each dictionary if the program closes.
                Runtime.getRuntime().addShutdownHook(new Thread(dictionary::saveDictionary));
            }

        return dictionaries;
    }

    // This method is used for proper error information.
    protected static void errorInfo(String exception){

        String output = "Error: " + exception + ". ";

        switch (exception){
            case "Wrong Number of Parameters":
                output += "It was expected at least two arguments, port number (integer) " +
                        "and dictionary name (string).";
                break;
            case "Wrong Input Type for Port Number":
                output += "It was expected an integer number for the port argument.";
                break;
            case "Wrong Port Number":
                output += "The number for the port number is not suitable. Please try with " +
                        "other port number (ex.: 3005).";
                break;
            case "Port Already in Use":
                output += "The port is already in use. Please try with other number.";
                break;
            case "Client Closes Connection":
                output += "The client " + clients + " has closed the connection.";
                output = output.replace("Error: ", "");
                break;
            default:
                output += "No tracked error.";
        }
        System.out.print(output);
    }
}