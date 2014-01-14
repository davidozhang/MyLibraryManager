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
import java.io.File;
import java.io.FileReader;

/*
 * This section performs the search functions for both the administrator and normal user 
 */

public class MyLibraryManager_Search {
	
	private static String[] resultString;
	private static String [] bookEntries;
	
	private JFrame frame;	//initiates GUI frame and panel 
	private JPanel p;	//initiates all textfields, labels, dropdown lists, and buttons 
	private JButton OK=new JButton("OK");
	private JButton CANCEL=new JButton("Cancel");
	private JTextField searchTextField=new JTextField(15);
	private JLabel searchTextFieldLabel=new JLabel("Enter your search term: ");
	
	/* MyLibraryManager_Search() 
	 * This method sets up the main search interface
	 * Pre: When initiated from either MyLibraryManager_Administrator or MyLibraryManager_User
	 * Post: The main search interface is created
	 */
	
	public MyLibraryManager_Search() {	
		
			p = new JPanel();		//create panel, buttons, drop down lists 
			p.setLayout(new FlowLayout());
			searchTextFieldLabel.setFont(new Font("Century Gothic",Font.BOLD, 14));
			OK.setFont(new Font("Century Gothic",Font.BOLD, 12));
			CANCEL.setFont(new Font("Century Gothic",Font.BOLD, 12));
			
			Box box = Box.createHorizontalBox();		//add certain items to GUI box
            box.add(Box.createHorizontalGlue());
            box.add(searchTextFieldLabel); box.add(searchTextField);
            Box box2 = Box.createHorizontalBox();
            box2.add(Box.createHorizontalGlue());
            box2.add(OK); box2.add(CANCEL);
			p.add(box);			//box, textfield and drop down list added to panel 
			p.add(box2);
			
			
			OK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					String searchText=searchTextField.getText();
						//search all fields
					if (!searchText.trim().equals("")) {
						searchFunction(searchText);	//initiate all field search method
						frame.setVisible(false);
						searchTextField.setText("");
					}
					else {	//return error message if text field is blank
						JOptionPane.showMessageDialog(frame,
				                   "The search field is blank.",
				                   "Error Message",
				                   JOptionPane.ERROR_MESSAGE);
						searchTextField.setText("");
					}
				}
			});
			
			CANCEL.addActionListener(new ActionListener(){		//close window 
				public void actionPerformed(ActionEvent e){
					frame.setVisible(false);
				}
			});
				
			frame = new JFrame("Search Book Entries");
			frame.setResizable(false);
			frame.add(p);
			frame.setSize(350,100);
			frame.setVisible(true);		//initiate window, let it be visible
	}//MyLibraryManager_Search()
	
	/* searchResultsWindow() 
	 * This method creates the search results window and displays the search results 
	 * Pre: Initiated from searchFunction()
	 * Post: The search results window is created, and search results are displayed 
	 */
	
	private static void searchResultsWindow() {
		final JFrame frame2=new JFrame("Search Results");	//create frame, panel, labels, buttons, and allocate space to store search results
    	JPanel p=new JPanel();  
    	p.setBackground(Color.DARK_GRAY);
    	JScrollPane scrollpane=new JScrollPane();
    	p.setLayout(new FlowLayout());
    	JLabel categories= new JLabel ("Unique Key | Book Title | Author Name | Year Published | Page Numbers | Publisher | Category | Book Rating");
    	categories.setFont(new Font("Century Gothic",Font.BOLD, 14));
    	categories.setForeground(Color.WHITE);
    	JLabel [] searchResults= new JLabel[resultString.length];
    	JButton OK=new JButton ("OK");	
    	JButton Back=new JButton("Back");
    	Box box = Box.createVerticalBox();
        box.add(Box.createVerticalGlue());
        box.add(categories);
        for (int i=0; i<resultString.length; i++) {
        	
        	searchResults[i]=new JLabel(resultString[i]);
        	searchResults[i].setFont(new Font("Century Gothic", Font.BOLD, 16));
        	searchResults[i].setForeground(Color.WHITE);
        	box.add(searchResults[i]);	//add box containing search results 
        }
    	Box box2=Box.createHorizontalBox();
    	box2.add(Box.createHorizontalGlue());
    	box2.add(OK); box2.add(Back);		//add boxes containg all GUI items with search results 
    	p.add(box2); p.add(box); p.add(scrollpane);
    	
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setResizable(true);
		frame2.add(p);
		frame2.setSize(800,150);
		frame2.setVisible(true);   
		
		OK.addActionListener(new ActionListener() {		//close window 
			public void actionPerformed(ActionEvent e) {
				frame2.setVisible(false);
			}
		});
		
		Back.addActionListener(new ActionListener(){		//re-initiate the search function
			public void actionPerformed(ActionEvent e){
				frame2.setVisible(false);
				MyLibraryManager_Search search=new MyLibraryManager_Search();
			}
		});
	}//searchResultsWindow() 
	
	/* libraryFileReader() 
	 * This method reads library.txt and stores each line read as single string
	 * Pre: //
	 * Post: Lines read from library.txt are saved into a single string, used for search
	 */
	
	private static void libraryFileReader() {
		
		bookEntries=new String[(MyLibraryManager_Library.data.size()/8)+1];

		for (int i=0; i<bookEntries.length; i++) {	//blank out all the array in bookEntries
    		bookEntries[i]="";
    	}
    	
		try {
			int i=0;
	    	File inputFile = new File("library.txt");	//read from library file
			String currentLine;
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			while((currentLine=reader.readLine())!=null) {	//get the book entries directly from the library file 
				if (!currentLine.equals("Unique|Title|Author|Year|Pages|Publisher|Category|Rating|") && !currentLine.trim().equals("")) {
				    bookEntries[i]=currentLine;	//store line read into a string
				    i++;
				}
			}
			reader.close();
    	}
    	catch (Exception e1) {
    		e1.printStackTrace();
    	}
	}//libraryFileReader()
	
	/* searchFunction() 
	 * This method searchs for matching term containing search term in all fields 
	 * Pre: Initiated from MyLibraryManager_Search()
	 * Post: The search results are stored into variable, displayed in searchResultsWindow()
	 */
	
	private static void searchFunction (String searchText) {
		
		libraryFileReader(); //obtain records as single strings
		resultString=new String [(MyLibraryManager_Library.data.size()/8)+1];		//set dynamic size array
		int j=0; 
		
		for (int i=0; i<(MyLibraryManager_Library.data.size()/8); i++) {
			resultString[i]="";		//initialize the array for storage of results
		} 
		
		for (int i=0; i<bookEntries.length; i++) {
			if (bookEntries[i].toUpperCase().contains(searchText.toUpperCase())) {	//if a vector element contains what the user is searching for (ignoring case)
				resultString[j]=resultString[j]+(bookEntries[i]);
				j++;
			}
		}
		
		searchResultsWindow();	//display the results
	}//searchFunction()
}

