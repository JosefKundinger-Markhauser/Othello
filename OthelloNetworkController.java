package othello;

/**
 * File Name:       OthelloNetworkController.java
 * Author:          Philip Thesen, ID# 040797646 & Josef Kundinger-Markhauser, ID# 040824003 
 * Course:          CST8221 - JAP, Lab Section: 302
 * Assignment:      2, Part 2
 * Date:            December 9 2020
 * Professor:       Daniel Cormier
 * Purpose:         This class is used to maintian a connection with a server                
 */

import java.io.*;
import java.net.*;
import java.util.*;

 /**
 * This Class is responsible to make a socket connection with the server
 * 
 * @author Philip Thesen
 * @author Josef Kundinger-Markhauser
 * @version 1.0
 * @see othello
 * @since 1.8.0_191
*/
public class OthelloNetworkController extends Thread
{
   /**Represent the main instance of the gui */
   OthelloViewController ovc;

   /**The socket connection */
   private Socket connection;

   /**The Client's username */
   private String userName;

   /**The stream used to listen to the server */
   InputStream inStream;

   /**The stream used to talk to the server */
   OutputStream outStream;

   /**Used to output to the server */
   PrintWriter out;

   /**Used to tell if the connection is good */
   boolean connected = false;

   /**The ip address of the server */
   String address;

   /**The ip port used to access the server */
   int port;

   /**Used to continue/end the main thread loop */
   boolean loop = true;

   /**Used to parse the server input */
   Scanner input;

   /**
	 * This method initializes network controller for the client
	 * 
	 * @param address - The ip address used to make a connection.
    * @param port - The ip port used to make the connection.
    * @param name - The username of the player.
    * @param frame - Access to the main GUI.   
	 */
   OthelloNetworkController(String address, int port, String name, OthelloViewController frame){
      ovc = frame;
      connection = null;
      userName = name;
      address = address;
      port = port;
      try
      {
            connection = new Socket(address, port);
            //connection.setSoTimeout(10000); // FUCK THIS SHIT
         try
         {
            inStream = connection.getInputStream();
            input = new Scanner(inStream);
            outStream = connection.getOutputStream();
            out = new PrintWriter(outStream, true /* autoFlush */);
         }
         catch (EOFException ee) 
         { 
            System.out.println(ee); 
            ovc.rightCenter.append("\nEnd of file exception");
            return;
         }
         
      }
      catch (UnknownHostException uhe) 
      { 
         System.out.println(uhe);
         ovc.rightCenter.append("\nUnknown host excatpion");
         return;
      }
      catch (ConnectException ce)
      {
         ovc.rightCenter.append("\nerror:SERVER NOT RUNNING???(error caugh by OthelloNetworkController");  
         return; 
      }
      catch (SocketTimeoutException ste)
      {
         //rare, but if the timer on the connection runs out
         ste.printStackTrace();
         ovc.rightCenter.append("\nSocet Timeout exception");
         return;
      }
      catch (IOException io)
      {
         io.printStackTrace();
         ovc.rightCenter.append("\nIO excatpion");
         return;
      }
      connected = true;
      ovc.connect.setEnabled(false);
   }

   /**
    Used to tell if the client is connected.
    @return The connected boolean status.
    */
   public boolean connected(){
      return connected;
   }

   /**
    Used to send string messages to the server
    @param message The string to be sent to the server.
    */
   public void toServer(String message){
      out.println(message);
   }

   /**
    * Used to disconnect from the server and sets the GUI up to be connected again if needed. 
    */
   public void disconnect(){
      loop = false;

      connected = false;
      ovc.disconnect.setEnabled(false);
      ovc.submitBtn.setEnabled(false);
      ovc.connect.setEnabled(true);
      ovc.rightCenter.append("\nDisconnected from server.");

      try{
         inStream.close();
         outStream.close();
         connection.close();
      }
      catch (IOException ioe)
      {
         System.out.println(ioe);
         ovc.rightCenter.append("\nIO exception");
      }
   }

   /**
    * Used for the thread that will continuously look for input from the server.
    */
   @Override
   public void run(){   
      String line;
      int checks = 0;
      toServer(userName);
      while (loop)
      {     
         out.print("");

         try{
            line = input.nextLine();
         }
         catch(NoSuchElementException e){
            if (checks < 10){
               checks++;
               continue;
            }
            else{
               ovc.rightCenter.append("\nServer no longer reachable");
               disconnect();
               break;
            }      
         }
         ovc.rightCenter.append("\n" + line);
      }
   }
}

