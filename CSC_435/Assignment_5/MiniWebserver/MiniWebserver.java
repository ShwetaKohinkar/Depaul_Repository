/* MiniWebserver.java

Copyright (C) 2020 with all rights reserved. Shweta Kohinkar

1.0 

Name: Shweta Kailas Kohinkar
Data: 2023-02-3
Java Version: 19.0.1


Command-line compilation:
> javac MiniWebserver

To run on localhost:
> java MiniWebserver   // run this file first to start the server


Files needed: MiniWebserver.java, WebAdd.html


For the MiniWebserver assignment answer these questions briefly in YOUR OWN
WORDS here in your comments:

1. How MIME-types are used to tell the browser what data is coming.
ANS: MIME types indicate the media type of data that is either supplied by web servers or web programs, or that is included in emails.
They're supposed to give users an indication about how the content should be handled and presented.
So, if we consider HTML doc, we use text/html
For plain text document, we use text/plain,
etc.
In our case, while sending response we have used text/html in Content type.


2. How you would return the contents of requested files of type HTML
(text/html)
Ans: We have to mention Content-Type: text/html

3. How you would return the contents of requested files of type TEXT
(text/plain)
Ans: We have to mention Content-Type: text/plain


*/

import java.io.*;
import java.net.*;

/**
 * MiniWebWorker Class
 */
class MiniWebWorker extends Thread
{
  Socket socket;          // Member variable

  MiniWebWorker(Socket s)// parameterized constructor
  {
    socket = s;
  }

  public void run()
  {
    PrintStream outPrintStream = null;   // Object declaration for writing streams over a socket
    BufferedReader inBufferedReader = null; // Object declaration for reading streams from a socket
    try
    {
      outPrintStream = new PrintStream(socket.getOutputStream());              // response is send to the server using this object
      inBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // reading request from client/browser

      String yourName = "", number_1 = "" , number_2 = "";
      for (int i = 0; i < 5; i++)  // reading response
      {
        String firstString = inBufferedReader.readLine();  // storing the input one by one in the string
        if (firstString!=null && firstString.contains("person") ) {
          yourName = firstString.split("&")[0].split("=")[1];                     // getting name from the input string
          number_1 = firstString.split("&")[1].split("&")[0].split("=")[1];// getting number 1 form the input string
          number_2 = firstString.split("&")[2].split("=")[1].split(" ")[0];// getting number 2 from the input string
          break;
        }
        else if(firstString.contains("MiniWebChecklist"))  // reading the MiniWebChecklist.html and displaying it on the browser
        {
          FileReader fileReader = new FileReader("MiniWebChecklist.html");
          BufferedReader buffRead = new BufferedReader(fileReader);// used bufferedreader to read the file
          StringBuilder readFile = new StringBuilder();
          String str;
          while( (str = buffRead.readLine()) != null) {
            readFile.append(str);                           // appending the lines one by one to a string builder
          }
          outPrintStream.println("HTTP/1.1 200 OK");
          outPrintStream.println("Content-Type: text/html \r\n\r\n"); // indicates the requested file type
          outPrintStream.println(readFile);
          break;
        }
//        else
//        {
//          System.out.println(firstString);
//          out.println("Received your message: "+ firstString);  // used to talk to MyTelnetClient
//        }
      }
      String HTMLResponse="";
      if(yourName!="") {
        System.out.println("HTML Response count: " + Integer.toString(MiniWebserver.state) + "\n" ); // displaying the conversation state on console
        String fullName = getFullName(yourName);
        HTMLResponse = displayForm(fullName, number_1,number_2);

      }
      outPrintStream.println("HTTP/1.1 200 OK"); // setting response code as OK in header
      outPrintStream.println("Content-Length: 600"); // Increased this value as submit button was not getting displayed on the form
      outPrintStream.println("Content-Type: text/html \r\n\r\n"); // type of response
      outPrintStream.println(HTMLResponse);

      socket.close(); // close the socket connection, but not the server;
    } catch (IOException x) {
      System.out.println("Error: Connetion reset. Listening again...");
    }
  }

  /**
   * function to get full name
   * @param objName
   * @return
   */
  private String getFullName(String objName) {
   return  objName.contains("+") ? objName.replace("+", " ") : objName;
  }

  /**
   * Function to create web form with the user entered name and 2 numbers
   * @param objName  used for storing the name
   * @param number_1 used for number 1
   * @param number_2 used for number 2
   * @return
   */
  private String displayForm(String objName, String number_1, String number_2)
  {
       String sum =  String.valueOf(Integer.parseInt(number_1) + Integer.parseInt(number_2));    // sum of two numbers
       String response =  "<FORM method=\"GET\" action=\"http://localhost:2540/WebAdd.fake-cgi\">\n" +
                          "<h1> Hello "+ objName +"...!!!</h1>\n" +
                          "Your total sum is: " + sum +
                          "\n" +
                          "<p>The conversation state is: " + MiniWebserver.state++ +  // maintaining conversation state
                          "<h2> WEB ADD </h2> \n" +
                          "\n" +
                          "Enter your name and two numbers. My program will return the sum:<p>\n" +
                          "\n" +
                          "<INPUT TYPE=\"text\" NAME=\"person\" size=20 value=\""+ objName +"\"> <P>\n" +    // printing name after submitting the form
                          "\n" +
                          "<INPUT TYPE=\"text\" NAME=\"num1\" size=5 value=\"" + number_1 +"\"> <br><br>\n" +    // printing num1
                          "<INPUT TYPE=\"text\" NAME=\"num2\" size=5 value=\"" + number_2 +"\"><p>\n" +          // printing num2
                          "\n" +
                          "<INPUT TYPE=\"submit\" VALUE=\"Submit Numbers\">\n" +
                          "<p>" +
                          "<a href=\"MiniWebChecklist.html\"target=\"_blank\">MiniWebChecklist file</a>" +
                          "\n" +
                          "</FORM>";
       return response;
  }

}

/**
 * Main Class
 */
public class MiniWebserver {

  static int state = 1;   // variable used for conversation state

  public static void main(String a[]) throws IOException {
    int q_len = 6;
    int port = 2540; // required port number
    Socket socket;

    ServerSocket serverSocket = new ServerSocket(port, q_len); // opening server connection on the required port

    System.out.println("Shweta Kohinkar's MiniWebServer running on port number: 2540");
    System.out.println("Point Firefox browser to http://localhost:2540/abc.\n");

    while (true) {
      socket = serverSocket.accept();      //  waiting for connection from client
      new MiniWebWorker(socket).start();
    }
  }
}

