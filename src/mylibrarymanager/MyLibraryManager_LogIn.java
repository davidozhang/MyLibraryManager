package mylibrarymanager;

/*
 * TIME STAMP: June 1, 2012 - 
 * LAST EDITED/REVIEWED BY: David Zhang
 * REPORT: Everything good to go.
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.util.Scanner;
	 
/*
 * This section creates the log-in interface, which connects to the database interface through successful log-in by user 
 */

public class MyLibraryManager_LogIn extends JPanel implements ActionListener {
	    private static String OK = "OK";	//initiates textfields, labels 
	    private static String CREATEACCOUNT = "Create Account";
	    public static String globalAccount;
	    public static JFrame controllingFrame; 
	    private JTextField userNameField;
	    private static JLabel welcome= new JLabel ("Please log-in as a user or admin, or create a new account.",SwingConstants.CENTER);
	    
	    /* MyLibraryManager_LogIn() 
		 * This method sets up the log-in interface
		 * Pre: When initiated from MyLibraryManager()
		 * Post: The main log-in interface is created
		 */
	    
	    public MyLibraryManager_LogIn (JFrame f) {
	        controllingFrame = f;
	 	        
	        userNameField = new JTextField(10);	//initiate textfield 
	        userNameField.setActionCommand(OK);
	        userNameField.addActionListener(this);
	        
	        JLabel name = new JLabel("Enter Your User Name: ");	//initiate label 
	        name.setFont(new Font("Century Gothic",Font.BOLD, 12));
	        name.setLabelFor(userNameField);
	 
	        JComponent buttonPane = createButtonPanel();
	 
	        JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
	        textPane.setLayout(new BoxLayout(textPane, BoxLayout.PAGE_AXIS));
	        textPane.add(name);
	        textPane.add(userNameField);
	        add(textPane);
	        add(buttonPane);
	    }
	 
	    protected JComponent createButtonPanel() {
	        JPanel p = new JPanel(new GridLayout(0,1));		//creates buttons, button panel, and add buttons to the panel 
	        JButton okButton = new JButton("OK");
	        okButton.setFont(new Font("Century Gothic",Font.BOLD, 14));
	        JButton createButton = new JButton("Create Account");
	        createButton.setFont(new Font("Century Gothic",Font.BOLD, 14));
	 
	        okButton.setActionCommand(OK);
	        createButton.setActionCommand(CREATEACCOUNT);
	        okButton.addActionListener(this);
	        createButton.addActionListener(this);
	 
	        p.add(okButton);
	        p.add(createButton);
	 
	        return p;
	    }
	    
	    public static boolean checkAccount (String input) {	//checks if the account exists or not 
	    	boolean checkpoint=false;	//default boolean is false 
	    	try {
				FileReader fr= new FileReader("accountNames.txt");	//reads from the list of account names registered 
				Scanner src= new Scanner(fr); 
				while (src.hasNext()) {
					if (src.nextLine().equalsIgnoreCase(input)) checkpoint=true;	//returns true if a match is found
				}
				src.close();
			}

			catch (Exception e) {	//if the accountNames.txt file is missing, the user will be notified 
				System.out.println("Notice from MyLibraryManager: Please add file 'accountName.txt' into the workspace folder " +
				"of MyLibraryManager to continue program operation.");
		    	System.exit(0);
			}	
			return checkpoint; 
	    }
	    
	    public void actionPerformed(ActionEvent e) {
	        String cmd = e.getActionCommand();
	        String input= userNameField.getText();
	        
	       if (OK.equals(cmd) && !input.trim().equals("")) {
	    	   if (checkAccount(input)==true) {	//if account exists
	    		   controllingFrame.setVisible(false);
	    		   globalAccount=input.toUpperCase(); 
	                MyLibraryManager_User user= new MyLibraryManager_User();	//initiates the main user database interface
	                resetFocus();
	                JOptionPane.showMessageDialog(controllingFrame,	//output welcome message 
	                "Welcome back, "+input.toUpperCase()+"!");
	            }
	    	   
	    	   	else if (checkAccount(input)==false && input.equalsIgnoreCase("Admin")) {	//if logged in as admin
	    	   		controllingFrame.setVisible(false);
	    	   		globalAccount=input.toUpperCase();
	    	   		MyLibraryManager_Administrator admin=new MyLibraryManager_Administrator();	//initiates the main admin database interface
	    	   		JOptionPane.showMessageDialog(controllingFrame,	//output welcome message 
	    	                "Welcome back, Administrator!");    	   		
	    	   	}
	    	   
	    	   	else if (checkAccount(input)==false) {	//output message if the user account does not exist 
	                JOptionPane.showMessageDialog(controllingFrame,
	                    "The user name entered does not exist. Please try again.",
	                    "Error Message",
	                    JOptionPane.ERROR_MESSAGE);
	                userNameField.setText("");
	            }
	       }
	      
	       	else if (CREATEACCOUNT.equals(cmd)) { //if user clicks Create Account
	       		MyLibraryManager_CreateAccount.createAndShowGUI();	   //show GUI for create account window     		
	       	}
	       	else {
	    		JOptionPane.showMessageDialog(controllingFrame,	//output message if the text field is blank.
	                    "The user name field is blank. Please try again.",
	                    "Error Message",
	                    JOptionPane.ERROR_MESSAGE);
	    				userNameField.setText("");
	    	}
	    }
	 
	    protected void resetFocus() {	//resets window focus on text field 
	        userNameField.requestFocusInWindow();
	    }
	 
	    public static void createAndShowGUI() {
	        JFrame frame2 = new JFrame("Log In to MyLibraryManager");	//creates main GUI interface 

	        final MyLibraryManager_LogIn newContentPane = new MyLibraryManager_LogIn (frame2);	//creates new instance of log-in model 
	        newContentPane.setOpaque(true); 
	        frame2.setContentPane(newContentPane); frame2.setPreferredSize(new Dimension (400,130));	//set attributes for window 
	        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame2.setLocationRelativeTo(null);
	        frame2.setResizable(false);
	        welcome.setFont(new Font("Century Gothic",Font.BOLD, 12));	//add welcome message
	        frame2.getContentPane().add(welcome,BorderLayout.EAST);
	        
	        frame2.addWindowListener(new WindowAdapter() {	//always reset focus on text field 
	            public void windowActivated(WindowEvent e) {
	                newContentPane.resetFocus();
	            }
	        });
	 
	        frame2.pack();
	        frame2.setVisible(true);	        //let it be visible 
	    }
}

