package server;

/**
 * File Name:       OthelloServerThread.java
 * Author:         	Philip Thesen, ID# 040797646 & Josef Kundinger-Markhauser, ID# 040824003 
 * Course:          CST8221 - JAP, Lab Section: 302
 * Assignment:      2, Part 2
 * Date:            December 9 2020
 * Professor:       Daniel Cormier
 * Purpose:         This class is used to maintian a connection with a client                
 */

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class is responsible for maintaining a connection with a client
 * 
 * @author Philip Thesen
 * @author Josef Kundinger-Markhauser
 * @version 1.0
 * @see othello
 * @since 1.8.0_191
*/
class OthelloServerThread extends Thread
{ 
    /**The socket the server will receive messages on */
    Socket sock;

    /**The userName of the client */
    String userName;

    /**The instance of the server that accepted this client */
    OthelloServer server;

    /**Output stream to the client */
    PrintWriter out;

    /**Input stream from the client */
    Scanner in;

    /**
      client thread constructor
      @param i The socket
      @param serv The server instance
    */
    public OthelloServerThread(Socket i, OthelloServer serv)
    { 
        sock = i; 
        server = serv;

        try{
            InputStream inStream = sock.getInputStream();
            OutputStream outStream = sock.getOutputStream();
            
            in = new Scanner(inStream);         
            out = new PrintWriter(outStream, true /* autoFlush */);
            try{
                userName = in.nextLine();
            }
            catch(NoSuchElementException e){
                e.printStackTrace();
                System.out.println("Error assigning userName");            
            }
            
        }
        catch (IOException e)
        {  
            e.printStackTrace();
            System.out.println(e);
        }
        server.print(userName + " has connected");
    }

    /**
     * Sends a message to the client
     * @param message The message to send to the client
     */
    public void display(String message){
        out.println(message);
    }

    /**
     * Gets the username of the client
     * @return The username of the client.
     */
    public String getUsername(){
        return userName;
    }

    /**
     * Disconnects the client from the server
     */
    public void disconnect(){        
        try{
            in.close();
            out.close();
            sock.close();
        }
        catch (IOException ioe)
        {
            System.out.println(ioe);
        }

        server.print(userName + " has disconnected");
    }

    /**
     * The thread that will continuously keep in contact with the client.
     */
    @Override
    public void run()
    {         
        display("Hello! You are connected to the server.");
        server.printEveryoneElse("SERVER: " + userName + " has joined the server", this);
        
        // echo client input
        boolean done = false;
        String line;
        int checks = 0;
        while (!done)
        {  
            out.print("");
            try{
                line = in.nextLine();
            }
            catch(NoSuchElementException e){
                if (checks < 10){
                    checks++;
                    continue;
                }
                else{
                    done = true;
                    server.removeClient(this);
                    server.broadcast("SERVER: " + userName + " has disconnected");
                    break;
                }           
            }

            if(line.length() > 1){
                if("/".equals(line.substring(0,1))){
                    if ("/bye".equalsIgnoreCase(line)){
                        done = true;
                        server.removeClient(this);
                        server.broadcast("SERVER: " + userName + " has disconnected");
                    }
                    else if("/who".equalsIgnoreCase(line)){
                        String[] names = server.who();
                        for(String s: names){
                            display(s);
                        }
                    }
                    else if("/help". equalsIgnoreCase(line)){
                        display("HELP:");
                        display("/help: this message.");
                        display("/bye : disconnect");
                        display("/who : Shows the names of all connected players.");
                        display("/name (name): rename yourself");
                    }
                    else if(line.length() > 6 && "/name".equalsIgnoreCase(line.substring(0,5))){
                        String tempName = userName;
                        userName = line.substring(6, line.length());
                        server.broadcast("SERVER: " + tempName + " Changed name to " + userName);
                    }
                    else{
                        server.broadcast(userName + ": " + line);
                    }
                }   
                else{
                        server.broadcast(userName + ": " + line);  
                    }        
            } 
        }
    }
}
