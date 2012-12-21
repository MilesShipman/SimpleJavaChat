import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
/**
 * This gui is the chat client for the program
 * writen by Miles Shipman
 */
 
public class GUI extends JPanel implements ActionListener {
    protected JTextField textField;
    static JTextArea textArea;
    //protected static JTextPane textArea;
    protected static ClientServer server; 
    private final static String newline = "\n";
    static int count; 
    static boolean first = false; 
    
 
    public GUI() {
        super(new GridBagLayout());
 
        //creates a new box at teh top of the program that the user can use to input data
        textField = new JTextField(40);
        textField.addActionListener(this);
        
        //creates a box that displays the data that the users type
        textArea = new JTextArea(40,40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
 
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
 
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
 
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }
 
    /**
     * the action preform handles the information that the user types and sends
     * it to the server. 
     */
    public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
        
        //save function saves a copy of the users interaction with their conversations and
        //saves them to a text file that the user can later relook at the conversation.
        if(text.equals("/save"))
        {
            try{
                String textpad = textArea.getText();
                String date = getTime();
                date = date.replaceAll(":","-");
                File outputFile = new File("H:/Compsci/CSCI395L/StudentsF12/milesS/Chat/" + date+".txt");
                FileWriter fileWriter = new FileWriter(outputFile);
                fileWriter.write(textpad);
                fileWriter.close();
            
            }catch(IOException e)
            {
                System.out.print(e);
            }
        
        }
        
        //sent me in the text and added the lines in the prototype cand continued in use. 
        else if(first){
            textArea.append("\nMe : " + text);
            textField.selectAll();
            server.sendMessage(text);
        }
        
        //prevent sending the ME: when the user ran the chat and name in the orginal prototype
        else if(!first){
            server.sendMessage(text);
        }
        
        first = true;
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    
    
    //gets the time for the server to use in save the file 
      public String getTime(){
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
    
    /**
     * Create the GUI and show it. 
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        frame.add(new GUI());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    
    /**
     * update is a while loop that is allways looking for incoming text messages from the server
     */
    public static void  update(){
        String line = "";
        boolean loop = true;
        while(loop)
        {
            line = server.receiveMessage(); 
            if(line != null){
                
                if(line.startsWith("\n@quit@now!"))
                {
                    System.exit(0);
                }
                else if(line == null){
                }
                else{
                textArea.append(line);
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
            }
        }
        
    }
    
    /**
     * This connects the GUI to the server and gains the username that the player will be known by
     */
    public static void Connect(){
        String name = JOptionPane.showInputDialog(null, "What do you want your UserName: "); 
        server = new ClientServer(textArea);
        server.connectToServer("10.1.48.66", textArea);
        server.sendMessage(name);
    }
   
    /**
     * The main thing that drives the game. 
     */
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        //javax.swing.SwingUtilities.invokeLater(new Runnable() {
           // public void run() {
                createAndShowGUI(); 
                Connect(); 
                update();
            //}
        //});
    }
}