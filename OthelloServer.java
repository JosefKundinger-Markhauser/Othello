package server;

/**
 * File Name:       OthelloServer.java
 * Author:          Philip Thesen, ID# 040797646 & Josef Kundinger-Markhauser, ID# 040824003 
 * Course:          CST8221 - JAP, Lab Section: 302
 * Assignment:      2, Part 2
 * Date:            December 9 2020
 * Professor:       Daniel Cormier
 * Purpose:         This class is a server that accepts connections from clients                
 */

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class is responsible for launching a multi-threaded server.
 * 
 * @author Philip Thesen
 * @author Josef Kundinger-Markhauser
 * @version 1.0
 * @see othello
 * @since 1.8.0_191
*/
public class OthelloServer extends Thread
{  
   /**Vector used to store all clients */
   Vector<OthelloServerThread> clientList = new Vector<>();

   /**The port number used to connect to the server */
   int port;

   /**
    * Creates instance of server and does error checking for the port number.
    * @param args The array of command line arguments.
    */
   public OthelloServer(String[] args){
      int tempPort;
      if (args.length == 0){
         System.out.println("Using defualt port: 62000");
         tempPort = 62000;
      }
      else{
         try{
            tempPort = Integer.parseInt(args[0]);
            System.out.println("Using port number: " + Integer.toString(tempPort));
         }
         catch(NumberFormatException e){
            System.out.println("Error: Invalid port number: " + args[0]);
            System.out.println("Using defualt port: 62000");
            tempPort = 62000;
         }
      }
      port = tempPort;
   }

   /**
    * The main
    * @param args The array of command line arguments.
    */
   public static void main(String[] args )
   {  
      OthelloServer othelloServer = new OthelloServer(args);
      othelloServer.start();
      
   }

   /**
    * The main thread the continuously looks to accept clients.
    */
   @Override
   public void run(){
      try
      {  
         int i = 1;
         ServerSocket s = new ServerSocket(port);

         while (true)
         {  
            Socket incoming = s.accept();
            System.out.println("Inbound connection #" + i);
            OthelloServerThread client = new OthelloServerThread(incoming, this);
            client.start();
            clientList.add(client);
            i++;
         }
      }
      catch (IOException e)
      {  
         e.printStackTrace();
      }
   }

   /**
    * Prints to the server command line.
    * @param message The string to print.
    */
   synchronized public void print(String message){
      System.out.println(message);
   }

   /**
    * Sends a message to every client.
    * @param message The string that will be sent to every client.
    */
   synchronized public void broadcast(String message){
      for(OthelloServerThread thread: clientList){
         thread.display(message);
      }
   }

   /**
    * Sends a message to all clients except the one passed through.
    * @param message The message that will be sent.
    * @param temp The client to not send the message to.
    */
   synchronized public void printEveryoneElse(String message, OthelloServerThread temp){
      for(OthelloServerThread thread: clientList){
         if(thread != temp){
            thread.display(message);
         }
      }
   }

   /**
    * Checks what all the client's usernames are.
    * @return A string array of all the usernames of all the clients.
    */
   synchronized public String[] who(){
      String[] names = new String[clientList.size() + 1];
      
      names[0] = "Currently on the server:";

      for(int i = 0; i < clientList.size(); i++){
         String name = clientList.get(i).getUsername();
         names[i + 1] = name;
      }

      return names;
   }

   /**
    * Removes a client from the server.
    * @param client The client that will be removed.
    */
   synchronized public void removeClient(OthelloServerThread client){
      String tempName = "";
      for(int i = 0; i < clientList.size(); i++){
         if (client == clientList.get(i)){
            tempName = clientList.get(i).getUsername();
            clientList.get(i).disconnect();
            clientList.remove(i);
            break;
         }
      }
   }
}
