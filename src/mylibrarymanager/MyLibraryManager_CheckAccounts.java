package mylibrarymanager;

/*
 * TIME STAMP: June 1, 2012 - 
 * LAST EDITED/REVIEWED BY: David Zhang
 * REPORT: Everything good to go.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

/*
 * This section performs the account checking functions for both the administrator and normal user 
 */

public class MyLibraryManager_CheckAccounts {
		
	private JFrame frame;	//initiate frame and panel for GUI 
	private JPanel p;
	private JLabel userNameLabel=new JLabel("Enter the user name: ");	//initial all textfields, labels, and buttons 
	private JButton OK=new JButton("OK");
	private JButton CANCEL=new JButton("Cancel");
	private static JTextField userNameTextField=new JTextField(15);
	public static String userNameText;
	public static String [] bookEntries= new String[50];	//used to store the returned results from user save file reading 
	
	/* MyLibraryManager_CheckAccounts() 
	 * This method sets up the main account checking interface
	 * Pre: When initiated from either MyLibraryManager_Administrator or MyLibraryManager_User
	 * Post: The main account checking interface is created
	 */
	
	public MyLibraryManager_CheckAccounts() {	
		p = new JPanel();
		p.setLayout(new FlowLayout());
		
		if (MyLibraryManager_LogIn.globalAccount.equalsIgnoreCase("Admin")) {	//only administrator can access all save files 
			
			userNameLabel.setFont(new Font("Century Gothic",Font.BOLD, 14));	//add labels, textfields and buttons to GUI panel 
			Box box = Box.createHorizontalBox();
            box.add(Box.createHorizontalGlue());
            box.add(userNameLabel); box.add(userNameTextField);
            Box box2 = Box.createHorizontalBox();
            box2.add(Box.createHorizontalGlue());
            box2.add(OK); box2.add(CANCEL);
			p.add(box);
			p.add(box2);
			
			
			OK.addActionListener(new ActionListener() {	//action performed when user presses OK 
				public void actionPerformed(ActionEvent e) {
					userNameText=userNameTextField.getText();	//get the user name entered by user 
					if (!userNameText.trim().equals("")) {
						if (MyLibraryManager_LogIn.checkAccount(userNameText)==true) {	//if user name exists
							frame.setVisible(false);
							accountFileReader(userNameText);	// reads the account information from the user name's asve file 
							bookEntriesWindow();
							userNameTextField.setText("");	//blanks the text field 
						}
						else {
							JOptionPane.showMessageDialog(frame,	//outputs error message if the user name entered does not exist 
				                    "The user name entered does not exist.",
				                    "Error Message",
				                    JOptionPane.ERROR_MESSAGE);
							userNameTextField.setText("");
						}
					}
					else {		//outputs error message if the text field is blank 
						JOptionPane.showMessageDialog(frame,
			                    "The user name field is blank.",
			                    "Error Message",
			                    JOptionPane.ERROR_MESSAGE);
						userNameTextField.setText("");
					}					
				}
			});
			
			CANCEL.addActionListener(new ActionListener(){	//closes the window 
				public void actionPerformed(ActionEvent e){
					frame.setVisible(false);
				}
			});
				
			frame = new JFrame("Check Account");	//spawn the window and let it be visible 
			frame.setResizable(false);
			frame.add(p);
			frame.setSize(350,100);
			frame.setVisible(true);
		}
		
		else {	//normal users can only access information from their own save account
			accountFileReader(MyLibraryManager_LogIn.globalAccount);
			bookEntriesWindow(); 
		}
	}//MyLibraryManager_CheckAccounts() 
	
	/* bookEntriesWindow() 
	 * This method sets up the result displaying window 
	 * Pre: When initiated from MyLibraryManager_CheckAccounts()
	 * Post: The content in user save file is displayed
	 */
	
	private static void bookEntriesWindow() {
		final JFrame frame2;	//initiates frame, panel, labels, buttons and scroll panel 
		if (MyLibraryManager_LogIn.globalAccount.equalsIgnoreCase("Admin")) frame2=new JFrame(userNameText.toUpperCase()+"'s Account Status");		
		else frame2=new JFrame(MyLibraryManager_LogIn.globalAccount.toUpperCase()+"'s Account Status");
	    JPanel p=new JPanel();  
	    JButton OK=new JButton ("OK");	
	    JButton Back=new JButton("Back");
	    
	    p.setBackground(Color.DARK_GRAY);
	    JScrollPane scrollpane=new JScrollPane();
	    p.setLayout(new FlowLayout());
	    JLabel categories= new JLabel ("Unique Key | Book Title | Author Name | Year Published | Page Numbers | Publisher | Category | Book Rating");
	    categories.setFont(new Font("Century Gothic",Font.BOLD, 14));
	    categories.setForeground(Color.WHITE);
	    final JLabel[] bookEntryLabels=new JLabel[bookEntries.length];	//create a storage space for results found based on the length of book entries read
	    Box box = Box.createVerticalBox();
	    box.add(Box.createVerticalGlue());
	    box.add(categories);
	    for (int i=0; i<bookEntries.length; i++) {	//add the results into the GUI box 
	    	bookEntryLabels[i]=new JLabel(bookEntries[i]);
	        bookEntryLabels[i].setFont(new Font("Century Gothic", Font.BOLD, 16));
	        bookEntryLabels[i].setForeground(Color.WHITE);
	        box.add(bookEntryLabels[i]);
	    }
	    Box box2=Box.createHorizontalBox();	
	    box2.add(Box.createHorizontalGlue());
	    box2.add(OK); 
	    if (MyLibraryManager_LogIn.globalAccount.equalsIgnoreCase("Admin")) box2.add(Back);	//only admin can repeatedly do search on different accounts
	    p.add(box2); p.add(box); p.add(scrollpane);	//add all boxes to the panel 

		frame2.setResizable(true);	//spawn the window and let it be visible
		frame2.add(p);
		frame2.setSize(800,150);
		frame2.setVisible(true);   
			
		OK.addActionListener(new ActionListener() {	//clear the array containing the book entries for later use 
			public void actionPerformed(ActionEvent e) {
				frame2.setVisible(false);
				for (int i=0; i<bookEntries.length; i++) {
			        bookEntries[i]="";		//clear array
				}
			}
		});
			
		Back.addActionListener(new ActionListener(){	//clear the array and re-initiate check account functions
			public void actionPerformed(ActionEvent e){	
				frame2.setVisible(false);
				for (int i=0; i<bookEntries.length; i++) {
			        bookEntries[i]="";		//clear array
				}
				MyLibraryManager_CheckAccounts check=new MyLibraryManager_CheckAccounts();		//re-initiate check account functions
			}
		 });
	}//bookEntriesWindow()
	
	/* accountFileReader() 
	 * This method reads the entries in the user save file as single strings
	 * Pre: //
	 * Post: Stores the entries as single strings
	 */
	
	private static void accountFileReader(String userNameText) {	
		String fileName="bookList_"+userNameText;
		
		try {
			int i=0;
	    	File inputFile = new File(fileName+".txt");	//reads from the user's save file 
			String currentLine;
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			while((currentLine=reader.readLine())!=null) {
			    bookEntries[i]=currentLine;	//store all information read into array 
			    i++;
			}
    	}
    	catch (Exception e) {	//catches exception and writes a user friendly warning
    		System.out.println("Notice from MyLibraryManager: Please add file "+fileName+".txt into the workspace folder " +
			"of MyLibraryManager to continue program operation.");
	    	System.exit(0);
    	}
	}//accountFileReader()
}
