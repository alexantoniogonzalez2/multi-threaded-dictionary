package client;
import utilities.Message;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client{

    public static void main(String[] args){

        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        try(Socket socket = new Socket(ip, port);){


            System.out.println("Connection established");

            // Get the input/output streams for reading/writing data from/to the socket
            Scanner scanner = new Scanner(System.in);
            String inputStr = null;

            // get the output stream from the socket.
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            ObjectInputStream in = new ObjectInputStream(inputStream);

            //While the user input differs from "exit"
            while (!(inputStr = scanner.nextLine()).equals("exit")){

                String[] input  = inputStr.split(" ");
                Message message;

                if (input[0].equals("add"))
                    message = new Message("add",input[1],input[2]);
                else
                    message = new Message(input[0],input[1],"");
                // Send the input string to the server by writing to the socket output stream
                //Message message = new Message("query",inputStr,"");

                out.writeObject(message);
                out.flush();
                System.out.println("Message sent");

                // Receive the reply from the server by reading from the socket input stream
                Message received = (Message)in.readObject(); // This method blocks until there  is something to read from the
                // input stream
                System.out.println("Received: " + received.toString());
            }

            scanner.close();
        }
        catch(UnknownHostException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException ex) {
            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}