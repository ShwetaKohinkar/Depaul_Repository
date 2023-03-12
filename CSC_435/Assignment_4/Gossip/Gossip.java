
/* 1.0 2023-03-06 Shweta Kailas Kohinkar

Name: Shweta Kailas Kohinkar
Data: 2023-03-06
Java Version: 19.0.1

Running these programs:

Run this program on different terminals to start the multiple processes

Command-line compilation:
> javac Gossip.java

To run on localhost:

> java Gossip 0 : //By using this command, we can run the Gossip node with ID 0.
> java Gossip 1 : //By using this command, we can run the Gossip node with ID 1.
> java Gossip 2 : //By using this command, we can run the Gossip node with ID 2.
We can run multiple instances of this Gossip program

Files needed: Gossip.java

Notes:
1. This program runs multiple Gossip Nodes
2. CLI commands:
    1. Enter 't' to know about all the commands.
    2. Enter 'l' to display Local Values.
    3. Enter 'p' to check neighbors are available or not.
    4. Enter 'm' for Minimum value and Maximum Value of the network.
    5. Enter 'c' to Calculate
    6. Enter 'v' to recreate random values
    7. Enter 'd' to delete current node
    8. Enter 'k' to kill entire network.
    9. Enter 'y' to display number of cycles.
    10. Enter 'n' to set the number of gossip messages and enter c for communication

*/


import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;


/**
 * Main class with the functions of console inputs
 */
public class Gossip {

    // Global variables
    public static int GossipNodeID = 0;
    public static float dataValue;
    public static float randomDataValue;
    public static float minimum = 0;
    public static float maximum = 0;
    public static float sizeValue = 0;
    public static float neighborSizeValue = 0;
    public static float neighborValue = 0;
    public static boolean trigger = false;
    public static int cycleNumber = 0;
    public static String stopServer = "";
    public static int msgCount = 20;

    /**
     * Main Function that starts the server and displays all the commands
     * @param args
     */
    public static void main(String args[]) {
        try {

            GossipNodeID = Integer.parseInt(args[0]); // Store the Gossip ID
            ResetValues();
            UDPServer objUDPServer = new UDPServer();
            objUDPServer.start();
            String input="";
            Thread.sleep(100);
            do {
                System.out.println("\n1. Enter 't' to know about all the commands.\n2. Enter 'l' to display Local Values\n" +
                        "3. Enter 'p' to check neighbors are available or not.\n4. Enter 'm' for Minimum value and Maximum Value of the network.\n" +
                        "5. Enter 'c' to Calculate\n6. Enter 'v' to recreate random values\n7. Enter 'd' to delete current node.\n" +
                        "8. Enter 'k' to kill entire network.\n9. Enter 'y' to display number of cycles.\n10. Enter 'n' to set the number of gossip messages.\n");
                Scanner in = new Scanner(System.in);
                input = in.nextLine();

                switch (input) {
                    case "t":
                        break;

                    case "l":
                        FunctionHandler.displayLocalValues();
                        break;

                    case "p":
                        FunctionHandler.displayPing();
                        break;

                    case "m":
                        FunctionHandler.displayMinMax();
                        break;

                    case "c":
                        Gossip.sizeValue = 1;
                        FunctionHandler.sendData();
                        break;

                    case "v":
                        FunctionHandler.recreateRandomVal();
                        break;

                    case "d":
                        FunctionHandler.deleteCurrNode();
                        break;

                    case "k":
                        FunctionHandler.killNetwork();
                        break;

                    case "y":
                        System.out.println(String.format("Number of cycles completed: %s", cycleNumber));
                        break;

                    case "n":
                        FunctionHandler.setMsgCount();
                        break;
                }
            }while(!input.equals("quit"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void ResetValues()
    {
        randomDataValue = new Random().nextInt(99);
        dataValue = randomDataValue;
        minimum = dataValue;
        maximum = dataValue;
        System.out.println("Data Value : " + Gossip.dataValue);
    }
}

class Ports{
    public static int GossipPortBase = 48100;

    public static int GossipPort = GossipPortBase + Gossip.GossipNodeID;

    public static int NextPort = GossipPort + 1;

    public static int PrevPort = GossipPort - 1;
}

/**
 * Class used to send the data over the network
 */
class Node implements Serializable{
    float dataValue;  // used to send the average data value
    float sizeValue;  // used to send network size
    String userString;
    int msgCount;
}

/**
 * This class handles all the command input operations
 */
class FunctionHandler {

    /**
     * Function to set the number of gossip messages sent to the neighbors
     */
    public static void setMsgCount() {
        System.out.println("\nEnter Message Count:");
        Scanner obj = new Scanner(System.in);
        String count = obj.nextLine();
        Gossip.msgCount = Integer.parseInt(count);  // take the user value to update the message count
        System.out.println(String.format("Message count set to %s", Gossip.msgCount));
    }

    /**
     * Function to display all the local values of a Node
     */
    public static void displayLocalValues() {
        System.out.println(String.format("Gossip ID: %s", Gossip.GossipNodeID));
        System.out.println(String.format("\nData Value: %s", Gossip.randomDataValue));
        System.out.println(String.format("Network Size: %s", (1 / Gossip.sizeValue)));
        System.out.println(String.format("Minimum Value: %s", Gossip.minimum));
        System.out.println(String.format("Maximum Value: %s", Gossip.maximum));
        System.out.println(String.format("Average Value: %s", Gossip.dataValue));
        System.out.println(String.format("Gossip Cycle Number: %s", Gossip.cycleNumber));
    }

    /**
     * Function to check is neighbor nodes are available for the gossip or not
     */
    public static void displayPing() {
        System.out.println("Next port is " + (UtilitySocket.getInstance().checkPort(Ports.NextPort) ? "not available" : "available"));
        System.out.println("Previous port is " + (UtilitySocket.getInstance().checkPort(Ports.PrevPort) ? "not available" : "available"));
    }

    /**
     * Function to display minimum and maximum value of the network
     */
    public static void displayMinMax() {
        System.out.println(String.format("Gossip ID: %s", Gossip.GossipNodeID));
        System.out.println(String.format("Minimum Value of the network: %s", Gossip.minimum));
        System.out.println(String.format("Maximum Value of the network: %s", Gossip.maximum));
    }

    /**
     * Function to recreate the random data value
     */
    public static void recreateRandomVal() {
        int newVal = new Random().nextInt(99);
        System.out.println(String.format("Old Value: %s", Gossip.randomDataValue));
        Gossip.randomDataValue = newVal;
        Gossip.dataValue = Gossip.randomDataValue;
        System.out.println(String.format("\nRecreated data value is: %s", Gossip.dataValue));

    }

    /**
     * Function to delete the current gossip node
     */
    public static void deleteCurrNode() {
        System.out.println("Killing current Node");
        System.exit(0);
    }

    /**
     * Function to kill the entire network
     */
    public static void killNetwork() {
        Gossip.stopServer = "stopserver";
        sendToPorts();
    }

    /**
     * Function to send kill message to all the neighbors
     */
    public static void sendToPorts() {
        if (!UtilitySocket.getInstance().checkPort(Ports.NextPort)) {
            UDPClient objNext = new UDPClient(Ports.NextPort);
            objNext.run();
        }
        if (!UtilitySocket.getInstance().checkPort(Ports.PrevPort)) {
            UDPClient objPrv = new UDPClient(Ports.PrevPort);
            objPrv.run();
        }
        System.out.println("Killing current Node...!!!");
        System.exit(0);
    }

    /**
     * Function to send data to the neighbors if available
     */
    public static void sendData() {
        try {
            Gossip.trigger = true;
            Gossip.cycleNumber++;
            for (int i = 0; i < Gossip.msgCount/2; i++) {
                if (!UtilitySocket.getInstance().checkPort(Ports.NextPort)) { // check if next port is available for gossip
                    UDPClient obj = new UDPClient(Ports.NextPort);
                    obj.run();
                    Thread.sleep(100);
                }
                if (!UtilitySocket.getInstance().checkPort(Ports.PrevPort)) { // check if previous port is available for gossip
                    UDPClient obj = new UDPClient(Ports.PrevPort);
                    obj.run();
                    Thread.sleep(100);
                }
            }
            Thread.sleep(2000);
            displayLocalValues();
            Gossip.trigger = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to calculate all the local values
     */
    public static void calculateValues() {
        Gossip.minimum = Math.min(Gossip.minimum, Gossip.neighborValue);
        Gossip.maximum = Math.max(Gossip.maximum, Gossip.neighborValue);
        Gossip.dataValue = (Gossip.dataValue + Gossip.neighborValue) / 2;
        Gossip.sizeValue = (Gossip.neighborSizeValue + Gossip.sizeValue) / 2;
        System.out.println(String.format("Average Size of Network: %s", Gossip.sizeValue));
        System.out.println(String.format("Average Data Value of Node %s: %s", Gossip.GossipNodeID, Gossip.dataValue));
    }
}

/**
 * Utility class for socket
 */
class UtilitySocket{

    private static UtilitySocket instance = new UtilitySocket();

    private UtilitySocket(){}

    public static UtilitySocket getInstance(){
        return instance;
    }

    /**
     * Function to get the Datagram socket port
     * @param port
     * @return
     */
    public DatagramSocket getServerSocket(int port)
    {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (IOException e) {
            datagramSocket.close();
        }
        return datagramSocket;
    }

    /**
     * Function to check if port is available or not
     * @param port
     * @return
     */
    public boolean checkPort(int port)
    {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Node getInputConnection(DatagramSocket socket,  DatagramPacket incomingPacket ) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(incomingPacket.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (Node) objectInputStream.readObject();
        } catch (Exception e) {

        }
        return null;
    }

    public void getOutputConnection(DatagramSocket socket, InetAddress IPAddress, int port, Node objNode)
    {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(objNode);

            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
            socket.send(sendPacket);
        }catch (Exception e)
        {

        }
    }
}

/**
 * Class to start the Datagram server which listens for the connection
 */
class UDPServer extends Thread {

    byte[] incomingData = new byte[1024];

    public void run() {
        System.out.println(String.format("Starting the Gossip Server using %s...!!! ", (Ports.GossipPort)));
        try {
            DatagramSocket keyServerSocket = UtilitySocket.getInstance().getServerSocket(Ports.GossipPort);
            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                keyServerSocket.receive(incomingPacket);
                new UDPWorker(keyServerSocket, incomingPacket).start();
            }
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
    }
}

/**
 * Worker class for UDPServer which receives data from other gossip nodes and calculates all the values as required
 */
class UDPWorker extends Thread {
    DatagramSocket socket;
    DatagramPacket incomingPacket;

    UDPWorker(DatagramSocket s, DatagramPacket _incomingData) {
        socket = s;
        incomingPacket = _incomingData;
    }

    public void run() {
        try {

            Node objNode = UtilitySocket.getInstance().getInputConnection(socket,incomingPacket);
            if (objNode.userString.equals("stopserver"))  // code to stop the node/network
            {
                Gossip.stopServer = "stopserver";
                FunctionHandler.sendToPorts();
            } else {
                System.out.println(String.format("\nReceived data value: %s", objNode.dataValue));
                System.out.println(String.format("Received size value: %s", objNode.sizeValue));
                Gossip.neighborValue = objNode.dataValue;
                Gossip.neighborSizeValue = objNode.sizeValue;
                Gossip.msgCount = objNode.msgCount;

                FunctionHandler.calculateValues();

                objNode.dataValue = Gossip.dataValue;
                objNode.sizeValue = Gossip.sizeValue;
                objNode.userString = Gossip.stopServer;
                objNode.msgCount = Gossip.msgCount;

                UtilitySocket.getInstance().getOutputConnection(socket, incomingPacket.getAddress(),incomingPacket.getPort(), objNode);

                if (!Gossip.trigger) {
                    FunctionHandler.sendData();
                }
            }
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
    }
}

/**
 * Class used as a client which sends the data to other gossip nodes
 */
class UDPClient {

    int port;

    UDPClient(int _port) {
        port = _port;
    }

    public void run() {

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");

            Node objNode = new Node();
            objNode.dataValue = Gossip.dataValue;
            objNode.sizeValue = Gossip.sizeValue;
            objNode.userString = Gossip.stopServer;
            objNode.msgCount = Gossip.msgCount;


            if (objNode.userString.equals("stopserver"))  // code to stop the node/network
            {
                Gossip.stopServer = "stopserver";
                UtilitySocket.getInstance().getOutputConnection(socket, IPAddress, port, objNode);
            } else {
                UtilitySocket.getInstance().getOutputConnection(socket, IPAddress, port, objNode);
                byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);

                objNode = UtilitySocket.getInstance().getInputConnection(socket,incomingPacket);

                System.out.println(String.format("\nReceived data value: %s", objNode.dataValue));
                System.out.println(String.format("Received size value: %s", objNode.sizeValue));
                Gossip.neighborValue = objNode.dataValue;
                Gossip.neighborSizeValue = objNode.sizeValue;
                Gossip.msgCount = objNode.msgCount;

                FunctionHandler.calculateValues();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

