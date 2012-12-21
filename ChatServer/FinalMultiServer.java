import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Write a description of class FinalMultiServer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FinalMultiServer
{
    static FinalMultiThread[] clientslist = new FinalMultiThread[0];
    static ArrayList <FinalMultiThread> CliList = new ArrayList<FinalMultiThread>(10);
    static Socket client; 
    static int current = 0;
    
    /**
     * Creates the server. 
     */
    public static void main(String args[])
    {
        client = null;
        ServerSocket server = null;
        
        try{
            System.out.print("\nCreating Server...\n");
            //creates the server
            server = new ServerSocket(8008);
            System.out.print("Created\n");
            //get the ip Address and the host name. 
            InetAddress localAddr = InetAddress.getLocalHost();
            System.out.println("IP address: " + localAddr.getHostAddress());
            System.out.println("Hostname: " + localAddr.getHostName());
            
        }catch(IOException e)
        {   
            // sends a 
            System.out.println("IO" +e); 
        }
        
        
        //constantly checks for a new aocket trying to attach itself to the trhead
        while(true)
        {
            try{
                client = server.accept();
                //create a new thread. 
                FinalMultiThread thr = new FinalMultiThread(client);
                System.out.print(client.getInetAddress()  + " : " + thr.getUserName() + "\n");
                CliList.add(thr);
                current++;
                thr.start();
            }catch(IOException e)
            {
                System.out.println(e);
            }
        }
    }
}
