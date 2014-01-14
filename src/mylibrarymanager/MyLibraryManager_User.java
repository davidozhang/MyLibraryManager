package mylibrarymanager;

/*
 * TIME STAMP: June 1, 2012 - 
 * LAST EDITED/REVIEWED BY: David Zhang
 * REPORT: Everything good to go. 
 */

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

/*
 * This portion of the program offers features that are available to normal users. 
 */

public class MyLibraryManager_User{
	
	 	private static JTextField bookReturnField=new JTextField(5);	//initiates the unique key entry field on the book return window 
		private static JTable table;
		private static Vector userData=new Vector();	//stores the data obtained from user save file in a Vector
		private static JFrame frame;
		
		private JButton search= new JButton ("Search");		private JButton checkAccount= new JButton ("Check Account");  //initiate all main buttons 
	    private JButton logOut= new JButton ("Log Out"); 	private JButton bookSignOut= new JButton ("Book Sign Out");		// on main interface
	    private JButton bookReturn=new JButton ("Book Return"); private JButton sort= new JButton ("Sort");
	    
	    public static String[] bookEntries=new String[50];	//stores all the book entries retrieved from account searching
	    public static String userFileName="bookList_"+MyLibraryManager_LogIn.globalAccount+".txt"; 	//stores the file name of the user 
	    public static String uniqueKey;
		private static int rowIndex;	//stores the row number selected by user
		private static int keyIndex;	//store the unique key entered by the user on the book return window 
		
		/* MyLibraryManager_User() 
		 * This method sets up the main interface for the user
		 * Pre: User logged in as a normal user 
		 * Post: The main user interface is created
		 */
		
	    public MyLibraryManager_User() {
	    	
            MyLibraryManager_Library model = new MyLibraryManager_Library();
	    	logOut.addActionListener(new logOutFunction());		//adds the appropriate function to all main interface buttons 
	    	search.addActionListener(new searchFunction());
	    	bookSignOut.addActionListener(new bookSignOutFunction());
	    	bookReturn.addActionListener(new bookReturnFunction());
	    	checkAccount.addActionListener(new checkAccountFunction());
	    	sort.addActionListener(new sortFunction());
	    	
	    	frame = new JFrame("MyLibraryManager - "+MyLibraryManager_LogIn.globalAccount.toUpperCase());	//initiates the main frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            Box box = Box.createHorizontalBox();	//adds all the buttons lined up nicely 
            box.add(Box.createHorizontalGlue());
            box.add(checkAccount); box.add(search); box.add(sort); box.add(bookSignOut); box.add(bookReturn); box.add(logOut);
            frame.add(box, BorderLayout.NORTH);
            table=new JTable();
            table.setModel(model);		//initiate a JTable based on the model from MyLibraryManager_Library()
            
            table.addMouseListener(new MouseAdapter() {	//add a listener for user mouse activity, and get the row number selected by mouse
            	public void mouseClicked(MouseEvent e) {
            		rowIndex = table.rowAtPoint(e.getPoint());
            	}
            });
            
            table.setPreferredScrollableViewportSize(new Dimension(1000, 200));		//set optimal size for JTable
            table.setFillsViewportHeight(true);
            table.setAutoCreateRowSorter(true);		//set automatic row sorter 
            
            JScrollPane scrollpane = new JScrollPane(table);	//set scroll panel 
            JPanel panel = new JPanel();
            panel.add(scrollpane);         
            frame.add(panel, "Center");
            frame.pack();
            frame.setVisible(true);	//let the frame become visible 

	    }//MyLibraryManager_User
	    
	    /* getUserData() 
		 * This method stores the information in user save file into a vector for use in this class
		 * Pre: Needs a vector of the information from user file 
		 * Post: The user save file is read and information is stored into a vectory 
		 */
	    
	    private static void getUserData() {
	    	String line;
	    	userData.clear();	//clear all possible remainings within the user data vector
	    	try {
	    		FileInputStream fis = new FileInputStream(userFileName);	//reads from user save file
	            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	            while ((line = br.readLine()) != null) {
	            		StringTokenizer st = new StringTokenizer(line, "|");
	                    while (st.hasMoreTokens())
	                            userData.addElement(st.nextToken());	//any item read is directly added to vector userData
	            }
	            fis.close(); 
	    	}
	        catch (Exception e) {
	        	System.out.println("Exception at: "+e);
	        }
	    }//getUserData()
	    
	    /* uniqueKeyChecker() 
		 * This method checks if the unique key entered by user exists in the user save file or not 
		 * Pre: Check if the unique key entered exists or not 
		 * Post: Returns true if unique key exists, false if it does not
		 */
	    
	    private static boolean uniqueKeyChecker(String uniqueKey) {
	    	boolean uniqueKeyValid=true; 
	    	getUserData(); //get the userData read from user save file 
	    	
	    	for (int i=0; i<userData.size();i++) {
	    		if (userData.size()==0) {
	    			uniqueKeyValid=false;	//the boolean is automatically set to false if no data exists
	    		}
	    		else {
		    		if (uniqueKey.equals((String) userData.elementAt(i))) {	//if the unique key entered by user exists, then set boolean to true
		    			keyIndex=i;	//the position value for the unique key found in userData is stored for later use
		    			uniqueKeyValid=true; break;
		    		}
		    		else uniqueKeyValid=false;
		    		i=i+7;
	    		}
	    	}
	    	return uniqueKeyValid;	//the boolean is returned
	    }//uniqueKeyChecker()
	    
	    /* userAccountFileReader() 
		 * This method reads each line in the user save file and treats them as a single line
		 * Pre: Reads the user save file
		 * Post: Stores each line read as a single string
		 */
	    
	    private static void userAccountFileReader() {
	    	
	    	for (int i=0; i<bookEntries.length; i++) {	//blank out all the array in bookEntries
	    		bookEntries[i]="";
	    	}
	    	
			try {
				int i=0;
		    	File inputFile = new File(userFileName);	//read from user save file
				String currentLine;
				BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				
				while((currentLine=reader.readLine())!=null) {	//get the book entries directly from the user save file 
				    bookEntries[i]=currentLine;
				    i++;
				}
				reader.close();
	    	}
	    	catch (Exception e1) {
	    		System.out.println("Exception at: "+e1);
	    	}
	    }//userAccountFileReader() 
	    
	    /* updateUserFile() 
		 * This method updates the user file based on the core vector data 
		 * Pre: If the user save file requires an update of information
		 * Post: User save file is updated
		 */
	    
	    private static void updateUserFile(Vector userData) {
	    	try {
	    		FileWriter fwriter=new FileWriter(userFileName);	//write to user save file
	    		BufferedWriter out=new BufferedWriter(fwriter);
	    		if(userData.size()!=0) {	//checks if something exists in the user save file
		    		for (int i=0; i<userData.size(); i++) {
		    			if (i+1!=((i+1)/8)*8) {
		    				out.write(userData.elementAt(i)+"|");	//data belonging to the same record are written on same line
		    			}
		    			else if (i+1==((i+1)/8)*8) {	//if record comes to an end, writes the last data and 
		    				out.write(""+userData.elementAt(i));	//makes two new lines to write the next record
		    				out.newLine();
		    				out.newLine();
		    			}
		    		}
	    		}
	    		else {
	    			out.write("");
	    		}
	    		out.close();
	    		
	    	}
	    	catch (Exception e) {
	    		System.out.println("Exception at: "+e);
	    	}
	    }//updateUserFile() 
	    
	    /* addToUserFile() 
		 * This method appends new entries to the user save file 
		 * Pre: If user has signed out a book entry into his/her account
		 * Post: Adds the entry into his/her save file 
		 */
	    
	    private static void addToUserFile(String signOutRecord){
	    	try {			
	    			FileWriter fwriter = new FileWriter (userFileName,true); 	//append to user save file
	    			BufferedWriter out = new BufferedWriter (fwriter);
					out.write(signOutRecord);	out.newLine();	out.newLine();	//writes into the file the entire book record signed out
					out.close();
					getUserData();	//update userData promptly
			}

			catch (Exception e) {
				System.out.println("Exception at "+e);
			}		
	    }//addToFile()
	    
	    /* bookReturnWindow() 
		 * This method initiates book return window and processes book return actions
		 * Pre: If user returns a book entry back into the library
		 * Post: Initiates appropriate book return actions/functions
		 */
	    
	    private static void bookReturnWindow() {
	    	    	
	    	final JFrame frame2=new JFrame("Book Return");	//initiates book return window frame
    		JPanel p=new JPanel();  
    	    p.setBackground(Color.DARK_GRAY);
    	    JScrollPane scrollpane=new JScrollPane();	//add scroll panel 
    	    p.setLayout(new FlowLayout());
    	    JLabel categories= new JLabel ("Unique Key | Book Title | Author Name | Year Published | Page Numbers | Publisher | " +
    	    								"Category | Book Rating");	//writes the category names at the top 
    	    categories.setFont(new Font("Century Gothic",Font.BOLD, 14)); categories.setForeground(Color.WHITE);
    	    
    	    final JLabel[] bookEntryLabels=new JLabel[bookEntries.length];
    	    JLabel bookReturnLabel=new JLabel("Enter the unique key number of the book to be returned: ");	//sets text field label 
    	    bookReturnLabel.setForeground(Color.WHITE);
    	    
    	    JButton OK=new JButton ("OK");	JButton Cancel=new JButton("Cancel");	//sets the buttons
    	    
    	    Box box = Box.createVerticalBox();
    	    box.add(Box.createVerticalGlue()); box.add(categories);	
    	    
    	    for (int i=0; i<bookEntries.length; i++) {	//outputs all the book entries within the user save file 
    	    	bookEntryLabels[i]=new JLabel("");
    	    	bookEntryLabels[i]=new JLabel(bookEntries[i]);
    	        bookEntryLabels[i].setFont(new Font("Century Gothic", Font.BOLD, 16));
    	        bookEntryLabels[i].setForeground(Color.WHITE);
    	        box.add(bookEntryLabels[i]);	//make sure the data is added to the GUI box
    	    }
    	    Box box2=Box.createHorizontalBox();
    	    box2.add(Box.createHorizontalGlue());
    	    box2.add(bookReturnLabel); box2.add(bookReturnField);
    	    box2.add(OK); box2.add(Cancel);
    	    p.add(box2); p.add(box); p.add(scrollpane);	//add boxes and scroll panel to the panel 

    		frame2.setResizable(true);
    		frame2.add(p);
    		frame2.setSize(800,150);
    		frame2.setVisible(true);   //let the frame become visible
    			
    		OK.addActionListener(new ActionListener() {
    			
    			public void actionPerformed(ActionEvent e) {
    				
    				String returnEntry="";	
    				uniqueKey=(String) bookReturnField.getText();	//reads the unique key from text field
    		    	if (!uniqueKey.trim().equals("") && uniqueKeyChecker(uniqueKey)==true) {	//proceeds if the unique key entered exists
    		    		frame2.setVisible(false);
    		    		for (int i=keyIndex; i<keyIndex+8; i++) {	//enters the to-be-returned item into a string called returnedEntry
    		    			if (i!=keyIndex+7) returnEntry=returnEntry+userData.elementAt(i)+"|";
    		    			else if (i==keyIndex+7) returnEntry=returnEntry+userData.elementAt(i);
    		    		}
    		    		
    		    		try {	 //adds the returned item into the library save file according to the library file writing conventions
	    	    			if (MyLibraryManager_Library.data.size()==1) {
	    	    				FileWriter fwriter = new FileWriter ("library.txt"); 
		    	    			BufferedWriter out = new BufferedWriter (fwriter);
	    	    				out.write("Unique|Title|Author|Year|Pages|Publisher|Category|Rating|");
	    	    				out.newLine(); out.newLine(); 
	    	    				out.write(returnEntry); out.newLine(); out.newLine();
	    	    				out.close();
	    	    			}
	    	    			else if (MyLibraryManager_Library.data.size()>1) {
	    	    				FileWriter fwriter = new FileWriter ("library.txt",true); 
		    	    			BufferedWriter out = new BufferedWriter (fwriter);
		    	    			out.write(returnEntry);out.newLine(); out.newLine(); 
		    	    			out.close(); 
	    	    			}
	
	    					MyLibraryManager_Administrator.dataChanged=true;	//updates the library table interface data
	    					MyLibraryManager_Library model=new MyLibraryManager_Library();
	    					table.setModel(model);
	    		    	}
	    		    	catch (Exception e1) {
	    		    		System.out.println("Exception at: "+e1);
	    		    	}	    		    	
	    		    	for (int i=0; i<8; i++) {
	    		    		userData.removeElementAt(keyIndex);
	    		    	}
	    		    	updateUserFile(userData);
	    		    	bookReturnField.setText("");
	    		    }
	    		    
    		    	else if (!uniqueKey.trim().equals("") && uniqueKeyChecker(uniqueKey)==false) {	//returns an error message if the unique key
    		    		JOptionPane.showMessageDialog(frame,										//entered does not exist
	    						"The unique key does not exist.",
	    						"Error Message",
	    						JOptionPane.ERROR_MESSAGE);
	    		    	bookReturnField.setText("");	    		    	
    		    	}
    		    	
	    		    else {		//returns an error message if the unique key field is blank 
	    		    	JOptionPane.showMessageDialog(frame,
	    						"The unique key field is blank.",
	    						"Error Message",
	    						JOptionPane.ERROR_MESSAGE);
	    		    	bookReturnField.setText("");
	    		    }
    			}
    		});
    		
    		Cancel.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {	//close window, clear unique key entry text field
    				frame2.setVisible(false);
    				bookReturnField.setText("");
    			}
    		});
	    }//bookReturnWindow()
	    
	    /* bookSignOutWindow() 
		 * This method processes book sign out functions
		 * Pre: If user signs out a book entry from the library
		 * Post: Initiates appropriate book sign out actions/functions
		 */
	    
	    private class bookSignOutFunction implements ActionListener	//initiates book sign out function
	    {
	    	String signOutRecord="";
	    	public void actionPerformed(ActionEvent e)
    		{
	    		if (rowIndex==-1) {	//returns error message if no row is selected, or main interface window is out of focus
	        		JOptionPane.showMessageDialog(frame,
							"You have not selected a book to sign out.",
							"Error Message",
							JOptionPane.ERROR_MESSAGE);
	        	}
	        	else {
	        		
	        		for (int i=8*rowIndex; i<8*rowIndex+8; i++) {	//add the book entry data from library into string called signOutRecord
	        			if(i!=8*rowIndex+7 && !((String)MyLibraryManager_Library.data.elementAt(i)).trim().equals(""))
	        			signOutRecord=signOutRecord+MyLibraryManager_Library.data.elementAt(i)+"|";
	        			else if (i==8*rowIndex+7) 
	        			signOutRecord=signOutRecord+MyLibraryManager_Library.data.elementAt(i);	        			
	        		}
	        		
	        		addToUserFile(signOutRecord);	//adds signOutRecord into user save file
	        		signOutRecord="";	//clears the variable
	        		
	        		if (MyLibraryManager_Library.data.size()>8) {	//removes the signed out book entry data from the library
		        		for (int i=0; i<8; i++) {
		        			MyLibraryManager_Library.data.removeElementAt(8*rowIndex);
		        		}		        		
		        		MyLibraryManager_Administrator.updateFile(MyLibraryManager_Library.data);
	        		}
	        		else if (MyLibraryManager_Library.data.size()==8) {
	        			for (int i=0; i<7; i++) {
		        			MyLibraryManager_Library.data.removeElementAt(rowIndex+1);
		        		}
	        			MyLibraryManager_Administrator.updateFile(MyLibraryManager_Library.data);	        			
	        			MyLibraryManager_Library.data.removeElementAt(0);
	        		}
	        		
	        		MyLibraryManager_Administrator.dataChanged=true;	//updates the library data promptly
					MyLibraryManager_Library model=new MyLibraryManager_Library();
					table.setModel(model);
	        	}
    		}
	    }//bookSignOutFunction()
	    
	    /* bookReturnFunction() 
		 * This method initiates book return window and functions
		 * Pre: If user clicks Book Return
		 * Post: Initiates appropriate book return actions/functions
		 */
	    
	    private class bookReturnFunction implements ActionListener 
	    {	    		    	
	    	public void actionPerformed(ActionEvent e)
    		{	    			 	    		
	    		userAccountFileReader();	//call up user save file account reader
	    		bookReturnWindow();	    	//call up the book return window and functions
    		}
	    }//bookReturnFunction()
	    
	    /* checkAccountFunction() 
		 * This method initiates check account window
		 * Pre: If user clicks Check Account
		 * Post: Initiates Check Account class 
		 */
	    
	    private class checkAccountFunction implements ActionListener 
	    {
	    	public void actionPerformed(ActionEvent e)
    		{
    			MyLibraryManager_CheckAccounts check=new MyLibraryManager_CheckAccounts(); 	//initiate check account interface and functions
    		}
	    }//checkAccountFunction()
	    
	    /* searchFunction() 
		 * This method initiates search class
		 * Pre: If user clicks Search
		 * Post: Initiates Search class and search windows/functions
		 */
	    
	    private class searchFunction implements ActionListener
    	{
    		public void actionPerformed(ActionEvent e)
    		{
    			MyLibraryManager_Search search=new MyLibraryManager_Search(); 	//initiate search interface and functions 
    		}
    	}//searchFunction
	    
	    /* sortFunction() 
		 * This method initiates sort functions
		 * Pre: If user clicks Sort
		 * Post: Initiates Sort class and sort windows/functions
		 */
	    
	    private class sortFunction implements ActionListener 
	    {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		MyLibraryManager_Library model=new MyLibraryManager_Library(); 
	    		table.setModel(model);
	    		MyLibraryManager_Sort sort=new MyLibraryManager_Sort(); 	//initiate sorting interface and functions 
	    	}
	    }//sortFunction()
	    
	    /* logOutFunction() 
		 * This method terminates program
		 * Pre: If user clicks Log Out
		 * Post: Terminates program 
		 */
	    
	    private class logOutFunction implements ActionListener
    	{
    		public void actionPerformed(ActionEvent e)
    		{   			
    			frame.setVisible(false);	//close window and address the user of the termination of program 
    			JOptionPane.showMessageDialog(frame,
                "You are now logged out. Thank you for using MyLibraryManager!"); 
    			System.exit(0);	//terminate the program 
    		}
    	}//logOutFunction 
}