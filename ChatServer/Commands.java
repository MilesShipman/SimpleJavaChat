import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Write a description of class Commands here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Commands
{
    // instance variables - replace the example below with your own
   

    /**
     * Constructor for objects of class Commands
     */
    public Commands()
    {
       
    }
    
    
    /**
     * prints out the help commands 
     */
    public void help(PrintWriter out)
    {
        send("@<username> : then your message for private messages",out);
        send("/save : saves the chat conversation",out);
        send("/quit : quits the program",out );
        send("/who : to see who is online at that moment",out);
        send("/who OP : shows who has server control",out);
    }

    /**
     * This searches for the thread with the username that matches then kickes them out of the server
     * 
     */
    public void kick(String name)
    {
        for(int j = 0; j<FinalMultiServer.CliList.size();j++)
                 {
                     FinalMultiThread thr = FinalMultiServer.CliList.get(j);
                     if(!thr.connected &&  (name.equals(thr.getUserName())))
                     {
                         thr.close();
                          // create a send all;
                     }
        }
    }
    
    /**
     * prints out the OP in the server so that people wants to know
     */
    public void showOP(PrintWriter out)
    {
        for(int j = 0; j<FinalMultiServer.CliList.size();j++)
        
                 {
                     FinalMultiThread thr = FinalMultiServer.CliList.get(j);
                     if(!thr.connected && thr.HeadOP == true)
                     {
                       send(thr.getUserName() + " is  Head OP",out); 
                     }
                     else if(!thr.connected && thr.OP == true)
                     {
                         send(thr.getUserName() + " is OP", out);
                     }
                 }
    }
    
    public void forceKick(String name)
    {
        for(int j = 0; j<FinalMultiServer.CliList.size();j++)
                 {
                     FinalMultiThread thr = FinalMultiServer.CliList.get(j);
                     if(!thr.connected && (thr.getUserName() != name))
                     {
                         thr.close();
                     }
                 }
    }
    
    public void privateMessage(String message,String name, PrintWriter out)
    {
        if(message.indexOf(" ") == -1){
                    send("Private message could not be sent",out);
                }
        else{
            String toUser = message.substring(1, message.indexOf(" "));
            for(int j = 0; j<FinalMultiServer.CliList.size();j++)
             {
                 FinalMultiThread thr = FinalMultiServer.CliList.get(j);
                 if(!thr.connected && (thr.getUserName() != name)&& (toUser.equals(thr.getUserName())))
                 {
                     thr.send("Private message from " + name + "  " +getTime() + " : " + message.substring(message.indexOf(" "),message.length()));
                 }
             }
         }
    }
    
    /**
     * Goes through thne server and gets all the usernames and prints them out and stating that they online
     *
     */
    public void showOnline(PrintWriter out, String name)
    {
        for(int j = 0; j<FinalMultiServer.CliList.size();j++)
                 {
                     FinalMultiThread thr = FinalMultiServer.CliList.get(j);
                     if(!thr.connected && thr.getUserName() != name)
                     {
                       send(thr.getUserName() + " is online", out); 
                     }
                 }
    }
    
    /**
     * Sends a message to ever thread and every client except the one who sent it
     */
    public void sendAll(String message, String name, PrintWriter out)
    {
        for(int j = 0; j<FinalMultiServer.CliList.size();j++)
                 {
                     FinalMultiThread thr = FinalMultiServer.CliList.get(j);
                     if(!thr.connected && (thr.getUserName() != name))
                     {
                         thr.send(name + "  " +getTime() + "    : " + message);
                     }
                 }
    }
    
    /**
     * sends a message back to the client that is assosiate with the thread
     */
    public void send(String message, PrintWriter out)
    {
        out.println(message);
        out.flush();
    }
    
    /**
     * gets the current time so the user knows when the message is sent
     */
    public String getTime(){
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
