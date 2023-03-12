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
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ColorClient
{
    private static int clientColorCount = 0;
    final List<String> allColorsPassed= new ArrayList<String>();  // Used to store all colors sent and received

    public static void main(String argv[])
    {
        ColorClient cc = new ColorClient(argv);
        cc.run(argv);
    }

    public ColorClient(String argv[])           // Parameterized Constructor
    {
        System.out.println("\nThis is the constructor if you want to use it.\n");
    }

    public void run(String argv[])
    {
        String serverName;

        if (argv.length < 1)                // checks if server is localhost or have entered an IP address
            serverName = "localhost";
        else
            serverName = argv[0];

        String colorFromClient = "";
        Scanner consoleIn = new Scanner(System.in);
        System.out.print("Enter your name:\n");
        System.out.flush();
        String userName = consoleIn.nextLine();     // used for getting a color from a console
        System.out.println("Hi " + userName);

        do
        {
            System.out.print("Enter a color, or quit to end:\n");
            colorFromClient = consoleIn.nextLine();
            if (colorFromClient.indexOf("quit") < 0)
            {
                // entered color name instead of 'quit'
                getColor(userName, colorFromClient, serverName);
            }
        } while (colorFromClient.indexOf("quit") < 0);             // runs the loop until client quits
        System.out.println("Cancelled by user request.");
        System.out.println(userName + ", You sent and received " + clientColorCount + " colors.");

        System.out.println("\nALL COLORS PASSED:");

        //Displaying all the unique colors sent and received
        for (int i= 0; i<allColorsPassed.size();i++)
        {
            System.out.println(allColorsPassed.get(i));  // getting all the colors from an ArrayList
        }
    }

    void getColor(String userName, String colorFromClient, String serverName)
    {
        try
        {
            ColorData objColor = new ColorData(); // ColorData class is defined in ColorServer. So, first compile ColorServer class.
            objColor.userName = userName;         // Assigning local variables to the ColorData's member variables.
            objColor.colorSent = colorFromClient;
            objColor.colorCount = clientColorCount;


            if(!allColorsPassed.contains(colorFromClient.toLowerCase()))
            {
                allColorsPassed.add(colorFromClient.toLowerCase());   // Adding colors to the list to display them after quit
            }

            // Socket socket = new Socket("JUNKhost", 45565); // Verifying an exception related to Unknown Host
            Socket socket = new Socket(serverName, 45565); // setting a hard coded port same as server
            System.out.println("\nWe have successfully connected to the ColorServer at port 45,565");

            OutputStream outStream = socket.getOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(outStream); // used for serializing the object.

            objOutStream.writeObject(objColor); // sending the serialized data object to the network
            System.out.println("We have sent the serialized values to the ColorServer's server socket");

            InputStream inStream = socket.getInputStream();                 // Getting an input stream from the socket
            ObjectInputStream objInStream = new ObjectInputStream(inStream);// Used for Deserializing an input stream

            ColorData inObj = (ColorData) objInStream.readObject(); // We now have access to the ColorData object sent by the client

            // Maintaining the state while using connectionless protocol
            clientColorCount = inObj.colorCount; // Saving sent and received color count in local variable.

            // Displaying the information sent by the Server to the Client
            System.out.println("\nFROM THE SERVER:");
            System.out.println(inObj.messageToClient);
            System.out.println("The color sent back is: " + inObj.colorSentBack);
            System.out.println("The color count is: " + inObj.colorCount + "\n");

            if(!allColorsPassed.contains(inObj.colorSentBack.toLowerCase()))
            {
                allColorsPassed.add(inObj.colorSentBack.toLowerCase());   // Adding colors to the list to display them after quit
            }

            System.out.println("Closing the connection to the server.\n");
            socket.close();        // Closing the connection
        }
        catch (ConnectException CE)
        {
            System.out.println("\nOh no. The ColorServer refused our connection! Is it running?\n");
            CE.printStackTrace();
        }
        catch (UnknownHostException UH)
        {
            System.out.println("\nUnknown Host problem.\n");
            UH.printStackTrace();
        }
        catch (ClassNotFoundException CNF)
        {
            // This exception is for the Class ColorData which is defined in the ColorServer file
            CNF.printStackTrace();
        }
        catch (IOException IOE)
        {
            IOE.printStackTrace();      // prints an error message on the terminal/console
        }
    }
}