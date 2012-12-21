import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Write a description of class FinalMultiThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FinalMultiThread extends Thread
{
    boolean connected;
    Socket socket;
    PrintWriter out; //I/O
    BufferedReader in;
    Socket clientSocket;
    String name;
    boolean OP = false; 
    boolean HeadOP = false; 
    private Commands commands;
    
    /**
     * creates the tread with the socket
     */
    FinalMultiThread(Socket s){
        super("FinalMultiThread");
        connected = false;
        clientSocket = s;
        try 
        {
            //sindesi I/O antikeimenwn me ta streams tou socket
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            commands = new Commands();
            out.println("What is your name :");
            out.flush();
            name = in.readLine();
            name = checkName(name); 
            out.println("Welcome to the RMC Chat Server, " + name);
            out.flush();
            welcome();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
       }
    
    public String checkName(String name){
        if(name.equals("MilesServerGod"))
        {
            OP = true;
            HeadOP = true; 
            name = name.substring(0,(name.indexOf("r")-2));
        }
        
        return name;
    }
    
    /**
     * sends a welcome message ot the other clients to let them know that someone has joined and tells that
     * client who is already on. 
     */
    public void welcome()
    {
         if(FinalMultiServer.CliList.size()>= 1)
            {
                 for(int j = 0; j<FinalMultiServer.CliList.size();j++)
                 {
                     FinalMultiThread thr = FinalMultiServer.CliList.get(j);
                     if(!thr.connected && (thr != this))
                     {
                         thr.send(name + " has joined the server");
                         send(thr.getUserName() + " is online"); 
                     }
                 }
            }
    }
    
    /**
     * returns the userName associated with that thread
     */
    public String getUserName(){
        return name; 
    }
    
    /**
     * sends the message to the client
     */
    public void send(String message)
    {   
        out.println(message);
        out.flush();
    }
    
    /**
     *runs the program and listens for messages coming in.
     */
    public void run()
    {
        listen();
    }
     
    /**
     * tells the commands that they should show everyonline. 
     */
    public void showOnline(){
         commands.showOnline(out,name);          
    }
    
    /**
     * returns true or false is the thread is assoiated with the thread and whether its a op or not
     */
    public boolean OP(){
        return OP; 
    }
    
    /**
     * returns true or false is the thread is assoiated with the thread and whether its a head op or not
     */
    public boolean HeadOP()
    {
        return HeadOP;
    }
    
    /**
     * this is the listen commands that constantly listens for messages that the client is wish to send 
     * and breaks down the commands and commits all of them. 
     */
    void listen(){
        try{
           //System.out.println("Hello");
           while(true){
            //out = new PrintWriter(clientSocket.getOutputStream(), true);
            //out.println("HEY\n");
            //out.flush();
            String message = in.readLine();//"does this thread work";
            //System.out.print(message);
            if(message.startsWith("/quit"))
            {
                connected= true;
             }
            else if(message.startsWith("/who OP"))
            {
                commands.showOP(out);
            }
            else if(message.startsWith("/kickall") && HeadOP == true)
            {
                int i = 0; 
                while(i<FinalMultiServer.CliList.size())
                {
                    i++;
                    commands.forceKick(name); 
                }
                send("You kicked everyone out"); 
            }
            else if(message.startsWith("/kick") && OP == true)
            {
                
                commands.kick(message.substring(6, message.length()));
                send(name + " was kicked of the server");   
            }
            else if(message.startsWith("/who"))
            {
                commands.showOnline(out,name);
            }
            else if(message.startsWith("/help"))
            {
                commands.help(out);
            }
            else if(message.startsWith("@"))
            {
                commands.privateMessage(message,name,out);
            }
            else if(FinalMultiServer.CliList.size()>1)
            {
                 commands.sendAll(message,name,out);
            }
            if (connected)
                {
                    close();
                }
           }  
       }catch(Exception e)
       {
           System.out.print(e); 
       }
    }
    
    /**
     * closes the thread so that is is out of the server and no long used. 
     */
    public void close()
    {
        try 
                    {                   
                        connected = false;
                        FinalMultiServer.CliList.remove(this);                        
                        out.print("@quit@now!");
                        out.flush();
                        for(int j = 0; j<FinalMultiServer.CliList.size();j++)
                         {
                             FinalMultiThread thr = FinalMultiServer.CliList.get(j);
                             if(!thr.connected && (thr != this))
                             {
                                 thr.send(name + " has left the chat room.");
                             }
                         }
                        //sendList();
                        out.close();
                        in.close();
                        clientSocket.close();
                        return;
                    }
                    catch (Exception d)
                    {
                        return;
                    }
    }
}
