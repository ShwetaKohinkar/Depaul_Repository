/*

Name: Shweta Kailas Kohinkar
Data: 2023-01-21
Java Version: 19.0.1

Command-line compilation:
> javac JokeServer.java
> javac JokeClient.java
> javac JokeClientAdmin.java

To run on localhost, use separate terminals:

> java JokeServer                          // this will open the port 4545 for JokeClient and 5050 for JokeClientAdmin and primary server will be up
> java JokeServer secondary                // By executing this command, secondary server will be up and will open a connection on 4546 for JokeClient and 5051 for JokeClientAdmin
> java JokeClient                          // By Executing this command, Joke Client will connect to primary server on port 4545
> java JokeClient localhost localhost      // By executing this command, JokeClient will have option to switch between servers which are opened on ports 4545 and 4546
> java JokeClientAdmin                     // By executing this command, JokeClientAdmin will connect to primary server on port 5050
> java JokeClientAdmin localhost localhost // By executing this command, JokeClientAdmin will have option to switch between servers which are opened on ports 4545 and 4546

Alternatively, to run over the Internet:

> javac JokeServer                              // need to compile JokeServer on that particular machine
> java JokeClient 192.168.0.167 192.168.0.167      [But use the actual IP address of the JokeServer]
> java JokeClientAdmin 192.168.0.167 192.168.0.167 [But use the actual IP address of the JokeServer]

[...]

Files needed: JokeServer.java, JokeClient.java, JokeClientAdmin.java

Notes:

This is program is used for getting the jokes and proverbs at the client side. We can use multiple clients at the same time.
This code was implemented using different class used for storage. We are also using JokeClientAdmin to change the Joke mode to proverb mode and vice versa.
JokeClient will just make request to JokeServer and JokeServer will sent a joke/proverb depending on the mode

The code has following implementations

1. On the JokeClient side, if we press <enter> it will receive a randomized joke/proverb depending on the mode.
2. I have added the following features:
    a. If the Joke server is down and JokeClient or JokeClientAdmin attempts to send a request, the process will not give an error or terminate; instead, it will attempt to connect until the server is up.
    b. Bragging rights point 3 - I’ve made client and server fault tolerant by writing the state to disk. As a result, if one crashes, they read the state back in from disk before resuming execution.
    c. Bragging rights point 2- I’ve implemented shutdown functionality in such a way that when JokeClientAdmin enter ‘shutdown’ on the console,
       Joke server will disconnect on a single request and JokeClient and JokeClientAdmin will not be able to request. If either attempts to send a request, they will have to wait for the server to be up.

--------------------

Thanks:


--------------------
*/

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class JokeClient {

    /**
     * Main Fucntion for JokeClient
     *
     * @param argv
     */
    public static void main(String argv[]) {
        JokeClient cc = new JokeClient();
        cc.run(argv);
    }

    /**
     * Function to execute JokeClient Code
     *
     * @param argv
     */
    public void run(String argv[]) {
        try {
            String serverName = "localhost";

            if (argv.length < 1)                // checks if server is localhost or have entered an IP address
                serverName = "localhost";
            else if (argv.length > 1)
                serverName = argv[0];

            Scanner consoleIn = new Scanner(System.in);
            System.out.print("Enter your name:\n");
            System.out.flush();
            String userName = consoleIn.nextLine();     // used for getting a username from a console
            System.out.println("Hi " + userName);

            JokeProverbData jokeData = new JokeProverbData();
            jokeData.userName = userName;
            String inputS = "";

            if (argv.length > 1) {
                //Printing message on console if both the servers are up
                System.out.println(String.format("Server one: %s, port %s Server two: %s, port %s", argv[0], Ports.PRIMARY_CLIENT_SERVER, argv[1], Ports.SECONDARY_CLIENT_SERVER));
            } else {
                //Printing message on console if only primary server is up
                System.out.println(String.format("Server one: %s, port %s", serverName, Ports.PRIMARY_CLIENT_SERVER));
            }
            int portNumber = Ports.PRIMARY_CLIENT_SERVER;
            do {
                System.out.print("Menu:\n1.Press 'enter key' to get next joke or proverb.\n2. Enter 's' to switch server.\n3. Enter 'quit' to end:\n");
                inputS = consoleIn.nextLine();
                if (inputS.equalsIgnoreCase("s") && argv.length > 1 &&
                        !SocketUtility.getInstance().checkPort(portNumber == Ports.PRIMARY_CLIENT_SERVER ? Ports.SECONDARY_CLIENT_SERVER : Ports.PRIMARY_CLIENT_SERVER)) {
                    serverName = serverName == argv[0] ? argv[1] : argv[0];    // Switching the server name when 's' is pressed
                    portNumber = portNumber == Ports.PRIMARY_CLIENT_SERVER ? Ports.SECONDARY_CLIENT_SERVER : Ports.PRIMARY_CLIENT_SERVER; // switching the port based on primary and secondary server

                    System.out.println(StorageUtility.getInstance().consoleOutput(portNumber, String.format("Now communicating with: %s, port: %s", serverName, portNumber)));

                    continue;
                } else if (inputS.equalsIgnoreCase("s")) // checking for secondary server
                {
                    System.out.println("No secondary server being used...!!!");
                    continue;
                }
                if (inputS.indexOf("quit") < 0) {
                    Socket socket = new Socket();
                    socket = SocketUtility.getInstance().checkingServerConnection(socket, serverName, portNumber);  // forming the connection on the required port

                    if (socket != null) {
                        ObjectOutputStream objOutStream = SocketUtility.getInstance().formOutputConnection(socket);
                        objOutStream.writeObject(jokeData); // sending the serialized data object to the network

                        ObjectInputStream objInStream = SocketUtility.getInstance().formInputConnection(socket);
                        JokeProverbData inObject = (JokeProverbData) objInStream.readObject();   // reading the object from the socket in byte streams and converting them into JokeProverbData object
                        System.out.println(StorageUtility.getInstance().consoleOutput(portNumber, String.format(inObject.joke_proverb)));
                    }
                    socket.close();
                }
            } while (inputS.indexOf("quit") < 0);
        } catch (IOException IOE) {
            IOE.printStackTrace();      // prints an error message on the terminal/console
        } catch (ClassNotFoundException CNF) {
            // This exception is for the Class ColorData which is defined in the ColorServer file
            CNF.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}