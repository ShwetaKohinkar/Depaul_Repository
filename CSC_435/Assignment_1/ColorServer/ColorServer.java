/*

Name: Shweta Kailas Kohinkar
Data: 2023-01-11
Java Version: 19.0.1

Command-line compilation:
> javac ColorServer.java
> javac ColorClient.java

To run on localhost, use separate terminals:

> java ColorServer
> java ColorClient
> java ColorClient

Alternatively, to run over the Internet:

> java ColorServer
> java ColorClient 192.168.0.167 [But use the actual IP address of the ColorServer]
[...]

Files needed: ColorServer.java, ColorClient.java

Notes:

1. We can run multiple Clients on separate terminals and the Server will keep all conversations separate. Each request is handles by a different thread
2. List of all unique colors sent and received will be displayed after 'quit' on the client side

--------------------

Thanks:

https://www.comrevo.com/2019/07/Sending-objects-over-sockets-Java-example-How-to-send-serialized-object-over-network-in-Java.html (Code dated 2019-07-09, by Ramesh)
https://rollbar.com/blog/java-socketexception/#
Also: Hughes, Shoffner and Winslow for Inet code.

--------------------
*/

import java.io.*;
import java.net.*;


class ColorData implements Serializable      // Using Serializable to convert state of an object into bit stream so that we can
                                             // send single bit one after another over the network.
{
    //Declaring all the member variables for class ColorData
    String userName;
    String colorSent;
    String colorSentBack;
    String messageToClient;
    int colorCount;
}

class ColorWorker extends Thread     // Creating a subclass
{
    Socket socket;                   // Declaring a class member which is local to ColorWorker class

    ColorWorker(Socket s)            // Defining a parameterized constructor
    {
        socket = s;
    }

    public void run()
    {
        try
        {
            InputStream inStream = socket.getInputStream();                     // Getting an input stream from the socket
            ObjectInputStream objInStream = new ObjectInputStream(inStream);    // Used for Deserializing an input stream

            ColorData inObject = (ColorData) objInStream.readObject();          // Reading and restoring an object's state for class ColorData,
                                                                                // Sent by the client.

            OutputStream outStream = socket.getOutputStream();                  // Returns an output stream
            ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);// Used for Serializing an output stream

            // Displaying the information sent by the client
            System.out.println("\nFROM THE CLIENT:\n");
            System.out.println("UserName: " + inObject.userName);
            System.out.println("Color sent from the client: " + inObject.colorSent);
            System.out.println("Connections count (State!): " + inObject.colorCount + 1);

            inObject.colorSentBack = getRandomColor();                          // Getting random color to sent back to client
            inObject.colorCount++;                                              // Increasing the color count sent to client
            inObject.messageToClient = "Thanks " + inObject.userName + " for sending the color " + inObject.colorSent;

            objOutStream.writeObject(inObject);                                 // Sending the serialized java object on the network.

            System.out.println("Closing the client socket connection...");
            socket.close();                                                      // Closing the connection
        }
        catch (ClassNotFoundException CNF)
        {
            CNF.printStackTrace();
        }
        catch (IOException x)
        {
            System.out.println("Sevrer error.");
            x.printStackTrace();
        }
    }


    String getRandomColor()
    {
        //Creating an array of different colors
        String[] colorArray = new String[]{"Red", "Blue", "Green", "Yellow", "Magenta", "Silver", "Aqua", "Gray", "Peach", "Orange"};

        int randomArrayIndex = (int) (Math.random() * colorArray.length);
        return (colorArray[randomArrayIndex]);     // returns color
    }
}


public class ColorServer {
    public static void main(String[] args) throws Exception {
        int q_len = 6;
        int serverPort = 45565;
        Socket socket;

        System.out.println("Shweta Kohinkar's Color Server 1.0 Starting up, listening at port " + serverPort + ".\n");

        ServerSocket serverSocket = new ServerSocket(serverPort, q_len); // Waiting for connection from client on the given port
        System.out.println("ServerSocket awaiting connections...");

        while (true)
        {
            socket = serverSocket.accept(); // Accepts the client connection

            System.out.println("Connection from " + socket); // Displaying details of socket
            new ColorWorker(socket).start();  // Creates a worker thread to run simultaneously on it and is also open for the next connection.
        }
    }
}


