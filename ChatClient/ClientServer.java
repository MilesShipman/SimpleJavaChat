import java.io.*;
import javax.swing.JOptionPane;
import java.net.*;
import javax.swing.*;

public class ClientServer implements Runnable{

    //private static DataInputStream input; 
    private static BufferedReader input; 
    private static PrintStream out; 
    boolean connected; 
    private static Socket client = null; 
    private static JTextArea text; 
    private Socket clientsocket; 
    
    /**
     * creates the clientserver and tries to may the JTextArea as substanable
     */
    public ClientServer(JTextArea text){
        text = text;
        
    }
    
    /**
     * an attempt to set the program JTextArea as substanable
     */
    public static void main(String[] args, JTextArea text)
    {
        text = text; 
    }
    
    /**
     * runs the server and was a testing ground.   
     */
    public void run()
    {
        String response;
        //System.out.print("HEY");
        try{
            while((response = input.readLine()) != null){
                System.out.print(response);
            }
            
        }catch(IOException e){
            System.err.println("IOException" + e);
        }
    }
    
    public void connectToServer(String host,JTextArea text){
        text = text; 
        text.append("Connecting to Server...\n"); 
        try{
            client = new Socket(host, 8008); 
            input = new BufferedReader( new InputStreamReader(client.getInputStream()));
            //input = new DataInputStream(client.getInputStream());
            out = new PrintStream(client.getOutputStream());
            //text.append("Connected");
            connected = true; 
            text.append("connect. \n"); 
            //run(); 
        }catch(IOException e){
            connected = false; 
            text.append("Unable to connect to server, please restart and try again\n"); 
        }
        
    }
    
    /**
     * shuts down the client without the server killing itself or crashing
     */
    public void socketClose()
    {
        try{
            client.close();
            input.close();
            out.close();
        }catch(Exception e){
        }
    }
    
    /**
     * Sends the messages to the server and flushes
     */
    public void sendMessage(String message){
            out.println(message);
            out.flush();
           }
    
           
    /** 
     * recieve messages checks to soo if there is a new message and returns it to the gui to
     * handle
     */
    public static String receiveMessage(){
        String response = null;
        //text.append("HEY");
        try{
          response = input.readLine(); 
          if(response == null)
          {
              response=""; 
          }
          
            /**
            try{
                while((response = input.readLine()) != null){
                    text.append(response);
                }
             */  
        }catch(IOException e){
            System.err.println("IOException" + e);
        }
        
        
        return ("\n" + response);
    }
}
