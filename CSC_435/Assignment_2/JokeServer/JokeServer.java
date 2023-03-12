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
import java.net.*;
import java.util.*;

class JokeProverbData implements Serializable  // Created a JokeProverb Data Class
{
    // Declaring the required variables
    String joke_proverb;
    int toggleMode;
    String userName;
    String uuid;
    boolean shutdown = false;
}

/**
Ports class has all the constants variables defined as a port
 */
class Ports
{
    static final int PRIMARY_CLIENT_SERVER = 4545;    // port for primary Server and Client
    static final int SECONDARY_CLIENT_SERVER = 4546;  // port for secondary Server and Client
    static final int PRIMARY_ADMIN_SERVER = 5050;     // port for primary Server and ClientAdmin
    static final int SECONDARY_ADMIN_SERVER = 5051;   // port for secondary Server and ClientAdmin
}

/**
 * ModeChanger class defined to set and get mode
 */
class ModeChanger {
    private static int mode = 0; //  variable for storing the mode

    /**
     * Function to change the mode as Joke or Proverb
     *
     * @return
     */
    public int SetMode() {
        mode = mode == 0 ? 1 : 0;
        return mode;
    }

    /**
     * Function to read the mode
     *
     * @return
     */
    public int GetMode() {
        return (mode);
    }
}

/**
 * Constructor for used for storing the mode and list of Joke or Proverb
 */
class ModeVal implements Serializable{
    // Declared all the required class variables
    String modeJoke;
    String modeProverb;
    List<String> jokeReceived;
    List<String> proverbReceived;
    String userName;

    ModeVal() //Constructor to initialize all the variables
    {
        jokeReceived = new ArrayList<>();
        proverbReceived = new ArrayList<>();
        modeJoke = "joke";
        modeProverb = "proverb";
        userName = "";
    }
}

class FileData implements Serializable{
    private Map<String, ModeVal> stateHistory = new HashMap<>(); // Defined a Map to store the state of user
    private Map<String, String> uuidList = new HashMap<>();

    public void writeObj(Map<String, ModeVal> _stateHistory, Map<String, String> _uuidList)
    {
        stateHistory = _stateHistory;
        uuidList = _uuidList;
    }

    public Map<String, ModeVal> getStateHistory() {
        return stateHistory;
    }

    public Map<String, String> getUuidList() {
        return uuidList;
    }
}

/**
 * StateofUser class use to store the state of every user
 */
class StateOfUser {
    private static Map<String, ModeVal> stateHistory = new HashMap<>(); // Defined a Map to store the state of user
    private static Map<String, String> uuidList = new HashMap<>();

    /**
     * Function to get the history of users
     *
     * @return
     */
    public Map<String, ModeVal> getStateHistory() {
        return stateHistory;
    }

    /**
     * Funciton to store the current value of the user
     *
     * @param _UUID   stores the user name
     * @param _objModeVal object for ModeVal
     * @return
     */
    public void setStateHistory(String _UUID, ModeVal _objModeVal) {
        stateHistory.put(_UUID, _objModeVal);
    }

    public void setStateFromFile(Map<String, ModeVal> map)
    {
        stateHistory = map;
    }
    /**
     * Function to set UUID for the user
     * @param user
     * @param uuid
     */
    void setUUID(String user, String uuid){
        uuidList.put(user,uuid);
    }

    String getUUID(String username){
        return uuidList.get(username);
    }
     public void setUuidListFromFile (Map<String, String> uuidMap)
     {
         uuidList = uuidMap;
     }
    public Map<String, String> getUuidList() {
        return uuidList;
    }
}

/**
 * Scoket Utility Class
 */
class SocketUtility {
    private static final SocketUtility instance = new SocketUtility();

    // private constructor to avoid client applications using the constructor
    private SocketUtility() {
    }

    /**
     * Function to get the instance of a class
     *
     * @return
     */
    public static SocketUtility getInstance() {
        return instance;
    }

    /**
     * Fucntion to get an object for ObjectOutputStream
     *
     * @param socket
     * @return
     */
    public ObjectOutputStream formOutputConnection(Socket socket) {
        try {
            OutputStream outStream = socket.getOutputStream();                   // Creating an output stream object
            ObjectOutputStream objOutStream = new ObjectOutputStream(outStream); // Used for Serializing an output stream which is to be sent
            return objOutStream;
        } catch (IOException IOE) {
            IOE.printStackTrace();      // prints an error message on the terminal/console
            return null;
        }
    }

    /**
     * Fucntion to get an object for ObjectInputStream
     *
     * @param socket
     * @return
     */
    public ObjectInputStream formInputConnection(Socket socket) {
        try {
            InputStream inStream = socket.getInputStream();                  // Getting an input stream from the socket
            ObjectInputStream objInStream = new ObjectInputStream(inStream); // Used for Deserializing an input stream
            return objInStream;
        } catch (IOException IOE) {
            IOE.printStackTrace();      // prints an error message on the terminal/console
            return null;
        }
    }

    /**
     * Fucntion to check the server connection
     *
     * @param socket
     * @param serverName Server name
     * @param portNumber port number currently being used
     * @return
     * @throws InterruptedException
     */
    public Socket checkingServerConnection(Socket socket, String serverName, int portNumber) throws InterruptedException {
        boolean check = true;
        while (check) {
            try {
                socket = new Socket(serverName, portNumber); // Forming the connection with the server
                check = false;
            } catch (ConnectException CE) {
                System.out.println("Waiting for JokeServer connection...!!!");  // waiting for server to connect
                Thread.sleep(3000);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }

    /**
     * Function to check of port is available or not
     *
     * @param port
     * @return
     */
    public boolean checkPort(int port) {
        try {
            ServerSocket socket = new ServerSocket(port); // checks if server is open or not
            socket.close();
        } catch (IOException e) {
            return false;   // return false if server is not open
        }
        return true;
    }
}

/**
 * Utility class for the project
 */
class StorageUtility {
    private static final StorageUtility instance = new StorageUtility();
    private static ServerSocket socket;
    private StorageUtility(){
    }

    /**
     * Function to return an instance of a class
     * @return
     */

    public static StorageUtility getInstance() {
        return instance;
    }

    public ServerSocket getServerSocket()
    {
        return socket;
    }
    public void setServerSocket(ServerSocket _socket)
    {
        socket = _socket;
    }
    /**
     * Function to return a string based on primary and secondary server
     * @param portNumber
     * @param msg
     * @return
     */
    public String consoleOutput(int portNumber, String msg)
    {
        if(portNumber == Ports.SECONDARY_ADMIN_SERVER || portNumber == Ports.SECONDARY_CLIENT_SERVER) {
            return "<S2> " + msg;  // prints secondary server messages
        }
        return msg; // prints primary server messages
    }
}

/**
 * Class for Worker Client
 */
class WorkerClient extends Thread {
    Socket socket;
    int clientPortNumber;

    WorkerClient(Socket _socket, int portNumber) {
        socket = _socket;
        clientPortNumber = portNumber;
    }

    /**
     * Function to run the WorkerClient
     */
    public void run() {
        try {

            ObjectOutputStream objOutStream = SocketUtility.getInstance().formOutputConnection(socket); // Output stream
            ObjectInputStream objInStream = SocketUtility.getInstance().formInputConnection(socket);   // Input stream

            objOutStream.writeObject(getData(objInStream));  // Sending the serialized java object on the network.
        } catch (IOException x) {
            System.out.println("Sevrer error.");
            x.printStackTrace();
        }
    }

    /**
     * Function for storing user related data
     * @param objInStream
     * @return
     */
    private JokeProverbData getData(ObjectInputStream objInStream) {

        // Creating all the required objects
        JokeProverbData objJokeProverb = new JokeProverbData();
        ModeChanger instance = new ModeChanger();
        StateOfUser objStateOfUser = new StateOfUser();
        FileData objFileData = new FileData();
        try {
            objJokeProverb = (JokeProverbData) objInStream.readObject();   // reading the received data
            if (objStateOfUser.getStateHistory().size() == 0) {
                File file = new File(String.format("Output_%s.bin", clientPortNumber));
                if (file.exists()) {              // checking if file exists
                    objFileData = readObjectToFile(file);// reading the data from a file
                    objStateOfUser.setStateFromFile(objFileData.getStateHistory()); // putting the data into map to store state of user before connecting
                    objStateOfUser.setUuidListFromFile(objFileData.getUuidList());
                }
            }
            if (objJokeProverb.uuid == null) {
                objJokeProverb.uuid = objStateOfUser.getUUID(objJokeProverb.userName) == null ? UUID.randomUUID().toString() : objStateOfUser.getUUID(objJokeProverb.userName);
                objStateOfUser.setUUID(objJokeProverb.userName, objJokeProverb.uuid);  // storing the UUID into a list
            }

            ModeVal objModeVal = objStateOfUser.getStateHistory().get(objJokeProverb.uuid);   // Get the user's data for the history
            if (objModeVal == null) {
                objModeVal = new ModeVal();
            }
            objModeVal.userName = objJokeProverb.userName;

            if (instance.GetMode() == 1) // Checking the mode
            {
                objJokeProverb.joke_proverb = getProverb(objModeVal);

            } else {
                objJokeProverb.joke_proverb = getJoke(objModeVal);
            }
            objStateOfUser.setStateHistory(objJokeProverb.uuid, objModeVal); // updating the map to get the user data
            objFileData.writeObj(objStateOfUser.getStateHistory(), objStateOfUser.getUuidList()); // writing an object to the file

            writeObjectToFile(objFileData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objJokeProverb;
    }
    /**
     * Function to get a randomized joke
     *
     * @param objModeVal
     * @return
     */

    private String getJoke(ModeVal objModeVal) {

        //Creating a List of different Jokes
        ArrayList<String> jokeList = new ArrayList<>();
        jokeList.add("JA <name-holder>: How do you make a lemon drop? Just let it fall.");
        jokeList.add("JB <name-holder>: What kind of tree fits in your hand? A palm tree");
        jokeList.add("JC <name-holder>: What falls in winter but never gets hurt? Snow.");
        jokeList.add("JD <name-holder>: How do we know that the ocean is friendly? It waves");

        jokeList.removeAll(objModeVal.jokeReceived);  // removing all the jokes that was already received

        int randomArrayIndex = (int) (Math.random() * jokeList.size());   // random index selection

        String joke = jokeList.get(randomArrayIndex);  // getting randomized joke from the remaining list

        if (jokeList.size() == 1) {  //checking if cycle is completed or not

            objModeVal.jokeReceived = new ArrayList<>();
            return joke.replace("<name-holder>", objModeVal.userName).concat("\n" + StorageUtility.getInstance().consoleOutput(clientPortNumber,"JOKE CYCLE COMPLETED"));
        }
        objModeVal.jokeReceived.add(joke);
        return joke.replace("<name-holder>", objModeVal.userName);  // putting username in the format
    }
    /**
     * Function to get a randomized proverb
     *
     * @param objModeVal
     * @return
     */
    private String getProverb(ModeVal objModeVal) {

        //Creating a List of different Proverbs
        ArrayList<String> proverbList = new ArrayList<>();
        proverbList.add("PA <name-holder>: When there’s smoke, there’s fire.");
        proverbList.add("PB <name-holder>: If it ain’t broke, don’t fix it.");
        proverbList.add("PC <name-holder>: You can’t have your cake and eat it too.");
        proverbList.add("PD <name-holder>: Time waits for no one.");

        proverbList.removeAll(objModeVal.proverbReceived);// removing all the proverbs that was already received

        int randomArrayIndex = (int) (Math.random() * proverbList.size());    // random index selection
        String proverb = proverbList.get(randomArrayIndex); //getting randomized proverb from the remaining list

        if (proverbList.size() == 1) {   //checking if cycle is completed or not
            objModeVal.proverbReceived = new ArrayList<>();
            return proverb.replace("<name-holder>", objModeVal.userName).concat("\n" + StorageUtility.getInstance().consoleOutput(clientPortNumber,"PROVERB CYCLE COMPLETED"));

        }
        objModeVal.proverbReceived.add(proverb);
        return proverb.replace("<name-holder>", objModeVal.userName);   // putting username in the format
    }


    /**
     * Function to write an object into a file
     * @param obj
     */
    public void writeObjectToFile(FileData obj)  {
        try {
            File f = new File(String.format("Output_%s.bin", clientPortNumber));
            FileOutputStream objFileOutStream = new FileOutputStream(f);
            ObjectOutputStream objObjectOutStream = new ObjectOutputStream(objFileOutStream);
            objObjectOutStream.writeObject(obj); // file writing object
            objObjectOutStream.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Function to read from a file
     * @param _file
     */
    public FileData readObjectToFile(File _file)  {
        FileData objFileData = null;
        try {
            FileInputStream objFileInputStream = new FileInputStream(_file);
            ObjectInputStream objObjectInputStream = new ObjectInputStream(objFileInputStream);
            objFileData = (FileData) objObjectInputStream.readObject(); // file reading object
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return objFileData;
    }
}

/**
 * Class for Worker Client Admin
 */
class WorkerClientAdmin extends Thread     // Creating a subclass
{
    Socket socket;                   // Declaring a class member which is local to ColorWorker class
    int serverPortAdmin;
    int serverClientSocket;

    /**
     * Function to run the WorkerClientAdmin
     */
    WorkerClientAdmin(Socket _socket, int _serverPortAdmin, int _serverClientSocket)            // Defining a parameterized constructor
    {
        socket = _socket;
        serverPortAdmin = _serverPortAdmin;
        serverClientSocket = _serverClientSocket;
    }

    public void run() {
        try {
            ModeChanger instance = new ModeChanger();
            JokeProverbData objJoke = new JokeProverbData();

            ObjectInputStream objectInputStream = SocketUtility.getInstance().formInputConnection(socket);
            JokeProverbData objJokeProverb = (JokeProverbData) objectInputStream.readObject();  // reading data from the socket
            if (objJokeProverb.shutdown) {
                System.out.println("Closing the server client connection");
                try {
                    StorageUtility.getInstance().getServerSocket().close();  // closing socket connection on shutdown
                } catch (Exception e) {
                }
            } else {
                instance.SetMode();   //setting mode as soon as ClientAdmin connects to the server
                System.out.println(StorageUtility.getInstance().consoleOutput(serverPortAdmin, String.format("Current Mode after JokeClientAdmin's Connection: %s", (instance.GetMode() == 0 ? "Joke" : "Proverb"))));
            }

            // sending toggle mode to the JokeClientAdmin
            objJoke.toggleMode = instance.GetMode();
            ObjectOutputStream objOutStream = SocketUtility.getInstance().formOutputConnection(socket);
            objOutStream.writeObject(objJoke);   // sending data on socket

        } catch (IOException x) {
            System.out.println("Sevrer error.");
            x.printStackTrace();
        } catch (ClassNotFoundException x) {
            System.out.println("Sevrer error.");
            x.printStackTrace();
        }
    }
}

/**
 *    Created a Class for Client Connection to run asynchronously with ClientAdmin
 */
class ThreadClient extends Thread {
        int q_len = 6;
        int serverPortClient;
        Socket socketClient;

        ThreadClient(int _serverClientPort) {
            serverPortClient = _serverClientPort;
        }

        @Override
        public void run() {
            try {
                System.out.println(StorageUtility.getInstance().consoleOutput(serverPortClient, String.format("Shweta Kohinkar's Joke Server 1.0 Starting up, listening at port %s", serverPortClient)));

                JokeProverbData obj = new JokeProverbData();
                ServerSocket serverSocket = new ServerSocket(serverPortClient, q_len); // Waiting for connection from client on the given port
                StorageUtility.getInstance().setServerSocket(serverSocket); // globally storing serversocket

                System.out.println(StorageUtility.getInstance().consoleOutput(serverPortClient, String.format("ServerSocket awaiting JokeClient connections on port: %s", serverPortClient)));

                while (true) {
                    socketClient = serverSocket.accept(); // Accepts the client connection

                    System.out.println(StorageUtility.getInstance().consoleOutput(serverPortClient, String.format("Connection from %s", socketClient)));

                    new WorkerClient(socketClient, serverPortClient).start();  // Creates a worker thread to run simultaneously on it and is also open for the next connection.
                }
            }catch (Exception e) {
                System.exit(0);
            }
        }
    }

/**
 *   Created a Class for ClientAdmin Connection to run asynchronously with Client
 */
class ThreadClientAdmin extends Thread
{
    int q_len = 6;
    int serverPortAdmin;
    Socket socketAdmin;
    int serverClientSocket;

    ThreadClientAdmin(int _serverPortAdmin, int _serverClientSocket)
    {
        serverPortAdmin = _serverPortAdmin;
        serverClientSocket = _serverClientSocket;
    }

    @Override
    public void run()
    {
        try {
            System.out.println(StorageUtility.getInstance().consoleOutput(serverPortAdmin, String.format("Shweta Kohinkar's Joke Server 1.0 Starting up, listening at port %s" , serverPortAdmin)));

            ModeChanger instance = new ModeChanger();

            ServerSocket serverSocket = new ServerSocket(serverPortAdmin, q_len); // Waiting for connection from client on the given port
            System.out.println(StorageUtility.getInstance().consoleOutput(serverPortAdmin, String.format("ServerSocket awaiting JokeClientAdmin connections on port: %s", serverPortAdmin)));

            System.out.println(StorageUtility.getInstance().consoleOutput(serverPortAdmin, String.format("Current Mode: %s", instance.GetMode() == 0 ? "Joke" : "Proverb" )));

            while (true) {
                socketAdmin = serverSocket.accept(); // Accepts the JokeClientAdmin connection
                System.out.println(StorageUtility.getInstance().consoleOutput(serverPortAdmin, String.format("Connection from %s", socketAdmin)));
                new WorkerClientAdmin(socketAdmin, serverPortAdmin,serverClientSocket).start();  // Creates a worker thread to run simultaneously on it and is also open for the next connection.
            }
        }catch (Exception e){
        }
    }
}

/**
 * MainClass
 */
public class JokeServer {
    public static void main(String[] args) throws Exception {
        int serverClientPort = 4545;
        int serverClientAdminPort = 5050;
        ServerSocket serverSocket = new ServerSocket();
        if (args.length == 0) {
            serverClientPort = Ports.PRIMARY_CLIENT_SERVER;
            serverClientAdminPort = Ports.PRIMARY_ADMIN_SERVER;
        } else if (args[0].toLowerCase().equals("secondary")) {
            try {
                serverSocket = new ServerSocket(Ports.PRIMARY_CLIENT_SERVER); // Waiting for connection from client on the given port
                System.out.println("\nPRIMARY server is not open...!!!\nPlease open primary server first to run Secondary Server.\n"); // Checking if primary server is open or not
                return;
            } catch (Exception ex) {
                serverClientPort = Ports.SECONDARY_CLIENT_SERVER;
                serverClientAdminPort = Ports.SECONDARY_ADMIN_SERVER;
            }
        }

        ThreadClient threadClient = new ThreadClient(serverClientPort);    // Thread for JokeClient
        ThreadClientAdmin threadClientAdmin = new ThreadClientAdmin(serverClientAdminPort,serverClientPort);  // Thread for JokeClientAdmin

        threadClient.start(); // starting threadClient
        threadClientAdmin.start();// starting threadClientAdmin
    }
}


