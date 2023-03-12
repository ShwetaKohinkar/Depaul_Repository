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
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class JokeClientAdmin
{
    /**
     * Main Function for JokeClientAdmin
     * @param argv
     */
    public static void main(String argv[])
    {
        JokeClientAdmin cc = new JokeClientAdmin();
        cc.run(argv);
    }

    /**
     * Function to execute JokeClientAdmin Code
     *
     * @param argv
     */
    public void run(String argv[]) {
        try {
            String serverName;
            int portNumber = Ports.PRIMARY_ADMIN_SERVER;
            String inputS = "";

            if (argv.length < 1)                // checks if server is localhost or have entered an IP address
                serverName = "localhost";
            else
                serverName = argv[0];

            if (argv.length > 1) {
                //Printing message on console if both the servers are up
                System.out.println(String.format("Server one: %s, port %s Server two: %s, port %s", argv[0], Ports.PRIMARY_ADMIN_SERVER, argv[1], Ports.SECONDARY_ADMIN_SERVER));
            } else {
                //Printing message on console if only primary server is up
                System.out.println(String.format("Server one: %s, port %s", serverName, Ports.PRIMARY_ADMIN_SERVER));
            }
            do {
                System.out.print("\nMenu:\n1. Press 'enter key' to change Joke or Proverb Mode.\n2. Enter 's' to switch server.\n3. Enter 'quit' to end.\n4. Enter 'shutdown' to shutdown\n");
                Scanner consoleIn = new Scanner(System.in);
                inputS = consoleIn.nextLine();
                if (inputS.equalsIgnoreCase("s") && argv.length == 2 &&
                        !SocketUtility.getInstance().checkPort(portNumber == Ports.PRIMARY_ADMIN_SERVER ? Ports.SECONDARY_ADMIN_SERVER : Ports.PRIMARY_ADMIN_SERVER))
                {
                    serverName = serverName == argv[0] ? argv[1] : argv[0];   // Switching the server name when 's' is pressed
                    portNumber = portNumber == Ports.PRIMARY_ADMIN_SERVER ? Ports.SECONDARY_ADMIN_SERVER : Ports.PRIMARY_ADMIN_SERVER; // switching the port based on primary and secondary server
                    System.out.println(StorageUtility.getInstance().consoleOutput(portNumber, String.format("Now communicating with: %s, port: %s" ,serverName, portNumber)));
                    continue;
                } else if (inputS.equalsIgnoreCase("s")) {
                    System.out.println("No secondary server being used...!!!");
                    continue;
                }
                if (inputS.indexOf("quit") < 0) {
                    Socket socket = new Socket();
                    socket = SocketUtility.getInstance().checkingServerConnection(socket, serverName, portNumber);
                    if (socket != null) {

                        ObjectOutputStream objOutStream = SocketUtility.getInstance().formOutputConnection(socket);
                        JokeProverbData inObj = new JokeProverbData();

                        if(inputS.equalsIgnoreCase("shutdown"))
                        {
                            inObj.shutdown = true;    // Shutdown the server
                        }
                        objOutStream.writeObject(inObj);

                         // reading the data object
                        if(!inputS.equalsIgnoreCase("shutdown")) {
                            ObjectInputStream objInStream = SocketUtility.getInstance().formInputConnection(socket);
                            inObj = (JokeProverbData) objInStream.readObject();
                            System.out.println(StorageUtility.getInstance().consoleOutput(portNumber, String.format("Current Mode on JokeServer: %s", inObj.toggleMode == 0 ? "Joke" : "Proverb")));
                        }
                    }
                }
            } while (inputS.indexOf("quit") < 0);
        } catch (UnknownHostException UH) {
            System.out.println("\nUnknown Host problem.\n");
            UH.printStackTrace();
        } catch (ConnectException CE) {
            System.out.println("\nOh no. The ColorServer refused our connection! Is it running?\n");
            CE.printStackTrace();
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