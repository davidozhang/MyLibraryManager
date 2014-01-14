package mylibrarymanager;

/*
 * TIME STAMP: June 1, 2012 - 
 * LAST EDITED/REVIEWED BY: David Zhang
 * REPORT: Everything good to go.
 */
	 
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import static mylibrarymanager.MyLibraryManager_LogIn.*;
	 
/*
 * This section creates the Create Account interface, which connects to the database interface through successful account creation by user 
 */

public class MyLibraryManager_CreateAccount extends JPanel implements ActionListener { //very similar configuration as the log-in window 
		
	    private static String OK = "OK";
	    private static String CANCEL = "Cancel";
	 
	    private JFrame controllingFrame; 
	    private JTextField userNameField;
	 
	    /* MyLibraryManager_CreateAccount() 
		 * This method sets up the Create Account interface
		 * Pre: When initiated from MyLibraryManager_LogIn()
		 * Post: The Create Window interface is created
		 */
	    
	    public MyLibraryManager_CreateAccount(JFrame f) {
	        controllingFrame = f;				//set frame, textfields and button panels 
	 	        
	        userNameField = new JTextField(10);
	        userNameField.setActionCommand(OK);
	        userNameField.addActionListener(this);
	        
	        JLabel name = new JLabel("Enter your desired user name: ");
	        name.setFont(new Font("Century Gothic",Font.BOLD, 12));
	        name.setLabelFor(userNameField);
	 
	        JComponent buttonPane = createButtonPanel();
	 
	        //Lay out everything.
	        JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
	        textPane.setLayout(new BoxLayout(textPane, BoxLayout.PAGE_AXIS));
	        textPane.add(name);
	        textPane.add(userNameField);
	 
	        add(textPane);
	        add(buttonPane);		//add to panel 
	    }
	 
	    protected JComponent createButtonPanel() {	//add buttons to panel
	        JPanel p = new JPanel(new GridLayout(0,1));
	        JButton okButton = new JButton("OK");
	        okButton.setFont(new Font("Century Gothic",Font.BOLD, 14));
	        JButton cancelButton = new JButton("Cancel");
	        cancelButton.setFont(new Font("Century Gothic",Font.BOLD, 14));
	 
	        okButton.setActionCommand(OK);
	        cancelButton.setActionCommand(CANCEL);
	        okButton.addActionListener(this);
	        cancelButton.addActionListener(this);
	 
	        p.add(okButton);
	        p.add(cancelButton);
	 
	        return p;
	    }
	    public static void createBookList (String input) {	//create the user's book list file 
	    	String textFileName="bookList_"+input;
	    	try{
	    		FileWriter fwriter = new FileWriter (textFileName+".txt"); 
				BufferedWriter out = new BufferedWriter (fwriter);
				out.close();
	    	}
	    	catch(Exception e) {
	    		System.out.println("Error at "+e);
	    	}
	    }
	    public static void saveAccountName(String input) {	//saves the account name registered
	    	try{
	    		FileWriter fwriter = new FileWriter ("accountNames.txt",true); 
				BufferedWriter out = new BufferedWriter (fwriter);
				out.append(input);	//writes all saved values into the text file 'saveData.txt'
				out.newLine();
				out.close();
	    	}
	    	catch(Exception e) {
	    		System.out.println("Error at "+e);
	    	}
	    }
	    
	    public void actionPerformed(ActionEvent e) {
	        String cmd = e.getActionCommand();
	        String input= userNameField.getText();
	        
	       if (OK.equals(cmd) && !input.trim().equals("")) { 
	    	   	if (checkAccount(input)==false && input.equalsIgnoreCase("Admin") || input.equalsIgnoreCase("Administrator")) {
	    	   		JOptionPane.showMessageDialog(controllingFrame,	//the account name cannot be admin or administrator 
	    		            "You may not use that as your user name. Please try another one.",
	    		            "Error Message",
	    		            JOptionPane.ERROR_MESSAGE);
	    		            userNameField.setText("");
	    	   	}
	    	   	else if (checkAccount(input)==false) {	//if the account does nto exist, log into database interface
	    		   controllingFrame.setVisible(false);
	    		   MyLibraryManager_LogIn.controllingFrame.setVisible(false);
	                JOptionPane.showMessageDialog(controllingFrame,	//output welcome message 
	                "Hello, "+input+"! Welcome to MyLibraryManager.");
	                globalAccount=input; 
	                saveAccountName(input);
	                createBookList(input);
	                MyLibraryManager_User user= new MyLibraryManager_User();	//initiate database interface 
	                resetFocus();
	    	    }
	            else if (checkAccount(input)==true){	//output error message if the acocunt name already exists 
		            JOptionPane.showMessageDialog(controllingFrame,
		            "The user name entered already exists. Please try another name.",
		            "Error Message",
		            JOptionPane.ERROR_MESSAGE);
		            userNameField.setText("");
		        }     
	        }

	    	else if (CANCEL.equals(cmd)) { //close the window 
	            controllingFrame.setVisible(false);
	            
	    	}
	    	else {
	    		JOptionPane.showMessageDialog(controllingFrame,	//output error message if the text field is blank 
	                    "The entry field is blank. Please try again.",
	                    "Error Message",
	                    JOptionPane.ERROR_MESSAGE);
	    		userNameField.setText("");
	    	}
		}
	 
	    protected void resetFocus() {	//resets window focus on text field 
	        userNameField.requestFocusInWindow();
	    }
	 
	    public static void createAndShowGUI() {	
	        //Create and set up the window.

	        JFrame frame = new JFrame("Create New Account");
	        
	        //Create and set up the content pane.
	        final MyLibraryManager_CreateAccount newContentPane = new MyLibraryManager_CreateAccount (frame);
	        newContentPane.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(newContentPane);
	        frame.setPreferredSize(new Dimension (400,130));
	        frame.setLocationRelativeTo(null);
	        frame.setResizable(false);
	        frame.addWindowListener(new WindowAdapter() {		        //Make sure the focus goes to the right component
		        														//whenever the frame is initially given the focus.
	            public void windowActivated(WindowEvent e) {
	                newContentPane.resetFocus();
	            }
	        });
	 
	        //Let the window be visibel
	        frame.pack();
	        frame.setVisible(true);	        
	    }
	}