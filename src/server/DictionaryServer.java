/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import utilities.Message;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;


import javax.net.ServerSocketFactory;

/**
 *
 * @author alex
 */
public class DictionaryServer {




    // Identifies the user number connected
    private static int counter = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Declare the port number
        int port = Integer.parseInt(args[0]);
        String file_name = args[1];
        Dictionary dictionary = new Dictionary(file_name);

        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try (ServerSocket server = factory.createServerSocket(port)){
            System.out.println("Waiting for client connection-");

            // Wait for connections.
            while (true){
                Socket client = server.accept();
                counter++;
                System.out.println("New connection for Client "+counter);

                // Start a new thread for a connection
                Thread t = new Thread(() -> serveClient(client, dictionary));
                t.start();
            }
        } catch(IOException e) {
            //e.printStackTrace();
        }
    }

    private static void serveClient(Socket client, Dictionary dictionary){
        try (Socket clientSocket = client) {


            System.out.println("Remote Hostname: " + clientSocket.getInetAddress().getHostName());
            System.out.println("Local Port: " + clientSocket.getLocalPort());

            //Get the input/output streams for reading/writing data from/to the socket

            // get the input stream from the connected socket
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            // create a DataInputStream so we can read data from it.
            ObjectInputStream in = new ObjectInputStream(inputStream);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);

            //Read the message from the client and reply
            Message clientMsg, answerMsg;

            try {
                while ((clientMsg = (Message)in.readObject()) != null){
                    System.out.println("Message client "+counter+":"+clientMsg.getType()+":"+clientMsg.getWord());
                    answerMsg = ProcessMessage(clientMsg,dictionary);
                    out.writeObject(answerMsg);
                    out.flush();
                    System.out.println("Response sent");
                }
            } catch(SocketException e){
                System.out.println("closed...");
            } catch (ClassNotFoundException ex){
                //Logger.getLogger(DictionaryServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            clientSocket.close();
        }
        catch (IOException e){
            //e.printStackTrace();
        }
    }

    protected static Message ProcessMessage(Message message, Dictionary dictionary){
        Message answer;
        Boolean word_exists;
        String type = message.getType();
        String word = message.getWord();
        String text = message.getText();

        switch(type){
            case "query":
                String meaning = dictionary.getMeaning(word);
                if (meaning != null)
                    answer = new Message("meaning",word,meaning);
                else
                    answer = new Message("unknown_word",word,"");
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
                answer = new Message("option", word, "");
                // code block
        }
        return answer;
    }
}
