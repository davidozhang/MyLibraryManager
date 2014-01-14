package mylibrarymanager;

/*
 * TIME STAMP: June 1, 2012 - 
 * LAST EDITED/REVIEWED BY: David Zhang
 * REPORT: Everything good to go.
 */


import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

import java.awt.event.*;

import javax.swing.event.*;
import javax.swing.table.*;

/*
 * Most features in this section are exclusive for administrator only. Normal users cannot access these features. 
 */

public class MyLibraryManager_Administrator extends JFrame{
	
	public static boolean dataChanged=false;	//boolean determines if the data has changed or not 
	
	private static JTable table;		//initiate JTable, frame and buttons
	private static JFrame frame; 
	private static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	
    private JButton create= new JButton ("Create"); 			private JButton delete= new JButton ("Delete");
    private JButton deleteAll= new JButton ("Delete All"); 		private JButton checkAccount= new JButton ("Check Account"); 
    private JButton search= new JButton ("Search"); 			private JButton logOut= new JButton ("Log Out"); 
    private JButton sort=new JButton("Sort");
    
	private static int rowIndex;
	private static int lastUniqueKey;
	
	private static String[] categoryStrings = {"-Select One-","---","Art","Autobiography","Biography","Children","Fantasy","Fiction","History","Literature",
											"Non-Fiction","Reference","Science Fiction","Other"};	
	private static String[] ratings={"-Select One-","---","1","2","3","4","5","6","7","8","9","10"};
	private static String[] years=new String[115];
	private static String[] pages=new String[1002];		//The 4 arrays here are used to store information in drop down lists
    
    /* MyLibraryManager_Administrator() 
	 * This method sets up the main interface for the administrator
	 * Pre: User logged in as administrator
	 * Post: The main administrator interface is created
	 */
    
    public MyLibraryManager_Administrator() {
    	
    	create.addActionListener(new createFunction()); 			delete.addActionListener(new deleteFunction());
    	logOut.addActionListener(new logOutFunction()); 			search.addActionListener(new searchFunction());
    	checkAccount.addActionListener(new checkAccountFunction()); deleteAll.addActionListener(new deleteAllFunction());
    	sort.addActionListener(new sortFunction());	//add function to main interface buttons 
    	
    	frame = new JFrame("MyLibraryManager - ADMINISTRATOR");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());		//add the buttons to a box in a nice manner, and add the box to panel 
        box.add(create);box.add(delete); box.add(deleteAll); box.add(sort); box.add(checkAccount); box.add(search); box.add(logOut);  
        
        frame.add(box, BorderLayout.NORTH);
               
        JPanel panel = new JPanel();
        MyLibraryManager_Library model = new MyLibraryManager_Library();
       
        table = new JTable();		//initiate JTable based on the model and attributes from MyLibrayManager_Library()
        table.setModel(model);   
        
        table.addMouseListener(new MouseAdapter() {	//listens to actions from the user's mouse to determine the row selected 
        	public void mouseClicked(MouseEvent e) {
        		rowIndex = table.rowAtPoint(e.getPoint());
        	}
        });
        
        setUpYearColumn(table, table.getColumnModel().getColumn(3));	//add drop-down lists to main interfaces 
        setUpPageColumn(table, table.getColumnModel().getColumn(4));
        setUpCategoryColumn(table, table.getColumnModel().getColumn(6));
        setUpRatingColumn(table, table.getColumnModel().getColumn(7));
    
        JScrollPane scrollpane = new JScrollPane(table);	//add a scroll panel 
        panel.add(scrollpane);
        
        table.setPreferredScrollableViewportSize(new Dimension(1000, 200));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        
        
        frame.add(panel, "Center");	//let the window become visible 
        frame.pack();
        frame.setVisible(true); 
    }//MyLibraryManager_Administrator()
    
    /* setUpYearColumn() 
	 * This method sets up the drop-down-list-enabled column for Year
	 * Pre: User clicks on a cell within 'Year'
	 * Post: A drop down list containing different options become available
	 */
    
    private void setUpYearColumn(JTable table, TableColumn yearColumn) {
    	int initialYear=1898, yearSum;
    	JComboBox comboBox = new JComboBox();
    	years[0]="-Select One-";	//adds the default initial selection to the drop-down list 
    	years[1]="---";
    	comboBox.addItem(years[0]); comboBox.addItem(years[1]);
    	
		for (int i=2; i<115;i++) {
			yearSum=initialYear+i;
			years[i]="";
			years[i]=years[i]+yearSum;	//adds the content in array into the drop-down  list 
			comboBox.addItem(years[i]);
		}
		
		yearColumn.setCellEditor(new DefaultCellEditor(comboBox));
			
		renderer.setToolTipText("Click for Options");
		yearColumn.setCellRenderer(renderer);	//adds a renderer to the drop down list 
    }//setUpYearColumn()
    
    /* setUpPageColumn() 
	 * This method sets up the drop-down-list-enabled column for Page
	 * Pre: User clicks on a cell within 'Page'
	 * Post: A drop down list containing different options become available
	 */
    
    private void setUpPageColumn(JTable table,TableColumn pageColumn) {
		JComboBox comboBox = new JComboBox();
    	pages[0]="-Select One-";	//adds the default initial selection to the drop-down list 
    	pages[1]="---";
    	comboBox.addItem(pages[0]); comboBox.addItem(pages[1]);
    	
		for (int i=2; i<1002;i++) {
			pages[i]="";
			pages[i]=pages[i]+(i-1);
			comboBox.addItem(pages[i]);	//adds the content in array into the drop-down  list 
		}
		pageColumn.setCellEditor(new DefaultCellEditor(comboBox));
			
		renderer.setToolTipText("Click for Options");
		pageColumn.setCellRenderer(renderer);	//adds a renderer to the drop down list 
	}//setUpPageColumn()
    
    /* setUpCategoryColumn() 
	 * This method sets up the drop-down-list-enabled column for Category
	 * Pre: User clicks on a cell within 'Category'
	 * Post: A drop down list containing different options become available
	 */
    
    private void setUpCategoryColumn(JTable table,TableColumn categoryColumn) {

		JComboBox comboBox = new JComboBox();
		for (int i=0; i<14;i++) {
			comboBox.addItem(categoryStrings[i]);	//adds the content in array into the drop-down  list  
		}
		categoryColumn.setCellEditor(new DefaultCellEditor(comboBox));
			
		renderer.setToolTipText("Click for Options");
		categoryColumn.setCellRenderer(renderer);	//adds a renderer to the drop down list 
	}//setUpCategoryColumn()
    
    /* setUpRatingColumn() 
	 * This method sets up the drop-down-list-enabled column for Rating
	 * Pre: User clicks on a cell within 'Rating'
	 * Post: A drop down list containing different options become available
	 */
    
    private void setUpRatingColumn(JTable table,TableColumn ratingColumn) {

		JComboBox comboBox = new JComboBox();
		
		for (int i=0; i<12;i++) {
			comboBox.addItem(ratings[i]);		//adds the content in array into the drop-down  list 
		}
		
		ratingColumn.setCellEditor(new DefaultCellEditor(comboBox));
			
		renderer.setToolTipText("Click for Options");
		ratingColumn.setCellRenderer(renderer);	//adds a renderer to the drop down list
    }//setUpRatingColumn()
    
    /* createWindow() 
	 * This method sets up the Create Entries window
	 * Pre: User clicks on 'Create' at the top
	 * Post: Create Entries window shows up, allowing the administrator to create new entries
	 */
    
    private void createWindow (JFrame f){	
    	final JComboBox categoryList=  new JComboBox(categoryStrings);   	//initiate drop down lists 
    	final JComboBox ratingList= new JComboBox(ratings);    	
    	final JComboBox yearList= new JComboBox(years);
    	final JComboBox pageList= new JComboBox(pages);
    	
    	final JFrame frame2=new JFrame("Create New Entry");	//initiate frame and panel 
    	final JTextField [] textField=new JTextField[3];
    	
    	JPanel p=new JPanel();  
    	p.setLayout(new FlowLayout());
    	   	
    	JLabel title=new JLabel("Book Title:");		textField[0]=new JTextField(20); 	//initiate labels and appropriate text fields for certain labels
    	
    	JLabel author=new JLabel("Author:");		textField[1]=new JTextField(20);
    	JLabel year= new JLabel("Year Published:");	
    	JLabel pages= new JLabel("Page Numbers:"); 	
    	JLabel publisher=new JLabel("Publisher:"); 	textField[2]=new JTextField(20);
    	JLabel category=new JLabel("Category:"); JLabel rating=new JLabel("Book Rating:");
    	JButton OK=new JButton ("OK");	JButton Cancel=new JButton("Cancel");		//initiate buttons 
    	
    	Box box = Box.createVerticalBox();
        box.add(Box.createVerticalGlue()); 	//glue all items vertically in a nice manner 
    	box.add(title); 	box.add(textField[0]); box.add(author); 	box.add(textField[1]); 
    	box.add(year); 		box.add(yearList);		box.add(pages); 	box.add(pageList); 
    	box.add(publisher); box.add(textField[2]); box.add(category); 	box.add(categoryList); 
    	box.add(rating); 	box.add(ratingList);
    	Box box2=Box.createHorizontalBox();
    	box2.add(Box.createHorizontalGlue()); 	
    	box2.add(OK); box2.add(Cancel);
    	p.add(box); p.add(box2);		//add boxes to panel 
    	
		frame2.setResizable(false);
		frame2.add(p);
		frame2.setSize(300,350);
		frame2.setVisible(true);   	//let the frame be visible
		
		OK.addActionListener(new ActionListener() {
			boolean checkpoint=true; 
			
			public void actionPerformed(ActionEvent e) {
				String titleField=(String) textField[0].getText();	//get the information entered by user in the text fields 
				String authorField=(String) textField[1].getText();
				String publisherField=(String) textField[2].getText();
				String yearField=(String) yearList.getSelectedItem();
				String pageField=(String) pageList.getSelectedItem();
				String categoryField=(String)categoryList.getSelectedItem();
				String ratingField=(String)ratingList.getSelectedItem();
				
				if (titleField.trim().equals("") || authorField.trim().equals("") || yearField.equals("-Select One-") 
					|| pageField.equals("-Select One-") || publisherField.trim().equals("") || categoryField.equals("-Select One-") 
					||ratingField.equals("-Select One-")) {	
					
					JOptionPane.showMessageDialog(frame,	//if one or more text field contains no information or 'select one', an error 
															//will be outputted
							"Please fill in all fields. If there is unknown information, select '---' from the options.",
		                    "Error Message",
		                    JOptionPane.ERROR_MESSAGE);	
					checkpoint=false;
				}
				
				if (checkpoint==true) {	//if all fields are filled 
					
					String bookRecord="";
					bookRecord=bookRecord+titleField+"|"+authorField+"|"+yearField+"|"+pageField+"|"+publisherField+"|"+categoryField+"|"+ratingField;
					addToFile(bookRecord);	//add the record to library.txt 
					dataChanged=true;		//notify the table model of changes
					MyLibraryManager_Library model = new MyLibraryManager_Library();
					table.setModel(model);
					setUpYearColumn(table, table.getColumnModel().getColumn(3));	//make sure the drop-down lists are still there after 
			        setUpPageColumn(table, table.getColumnModel().getColumn(4));	//the table is notified of change
			        setUpCategoryColumn(table, table.getColumnModel().getColumn(6));
			        setUpRatingColumn(table, table.getColumnModel().getColumn(7));
					frame2.setVisible(false);
					
				}
				else {
					if (checkpoint==false) checkpoint=true; 		//reset boolean 							
				}
			}
		});
		
		Cancel.addActionListener(new ActionListener(){		//close window 
			public void actionPerformed(ActionEvent e){
				frame2.setVisible(false);
			}
		});
    }//CreateWindow()      
       
    /* updateFile() 
	 * This method updates the content in save file library.txt
	 * Pre: When the data within the database is changed
	 * Post: Updates the content in save file library.txt
	 */
    
    public static void updateFile(Vector data) {
    	
    	try {
    		int counter=1;
    		FileWriter fwriter = new FileWriter ("library.txt"); 	//overwrites the library save file 
			BufferedWriter out = new BufferedWriter (fwriter);
			out.write("Unique|Title|Author|Year|Pages|Publisher|Category|Rating|");
			out.newLine(); out.newLine(); 
			
			if (MyLibraryManager_Library.data.size()==1) {
				out.write(""+MyLibraryManager_Library.data.elementAt(0));	//writes the unique key of the last entry for reference later 
				out.close();
			}
			else {
				for (int i=0; i<MyLibraryManager_Library.data.size(); i++) {
					if ((i+1) !=(((i+1)/8)*8) && !((String) MyLibraryManager_Library.data.elementAt(i)).trim().equals("-Select One-") 
						&& !((String) MyLibraryManager_Library.data.elementAt(i)).trim().equals("")) {
						out.write(MyLibraryManager_Library.data.elementAt(i)+"|");	//if element not equal to blank or 'select one'
																					//write full element into file 
					}
					else if ((i+1)!=(((i+1)/8)*8) && ((String)MyLibraryManager_Library.data.elementAt(i)).trim().equals("")
							|| ((String) MyLibraryManager_Library.data.elementAt(i)).trim().equals("-Select One-")) {
						out.write("---|");		//if element equals 'select one', replace it with '---'  and write into file 
					}
					else if ((i+1)==(((i+1)/8)*8) && !((String) MyLibraryManager_Library.data.elementAt(i)).trim().equals("")) {
						out.write(""+MyLibraryManager_Library.data.elementAt(i));
						out.newLine(); out.newLine();	//new lines added at the end of each record
					}
					else if ((i+1)==(((i+1)/8)*8) && ((String) MyLibraryManager_Library.data.elementAt(i)).trim().equals("")
							|| ((String)MyLibraryManager_Library.data.elementAt(i)).trim().equals("-Select One-")) {
						out.write("---");	//if the last element is 'select one' replace it with '---' and add new lines 
						out.newLine(); out.newLine();
					}
				}
				out.close();
			}
    	}
    	catch(Exception e) {
    		System.out.println("Exception at "+e);
    	}
    }//updateFile()
    
    /* getLastUniqueKey() 
	 * This method obtains the most recent unique key from save file 
	 * Pre: the user wants to create a new entry with a distinct unique key
	 * Post: the current unique key is obtained by adding 1 to the last unique key 
	 */
    
    private static void getLastUniqueKey() {
    	try {
    		File inputFile = new File("lastUniqueKey.txt");	//read from user save file
			String currentLine;
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			lastUniqueKey=Integer.parseInt(reader.readLine());
			reader.close();
    	}
    	catch (Exception e1) {
    		System.out.println("Exception at: "+e1);
    	}
    }//getLastUniqueKey()
    
    /* updateUniqueKey() 
	 * This method updates the unique key value in the save file 
	 * Pre: The most recent unique key is created
	 * Post: Added to lastUniqueKey.txt
	 */
    
    private static void updateUniqueKey(int currentUniqueKey) {
    	try {
    		FileWriter fwriter=new FileWriter("lastUniqueKey.txt");	//writes the most recent unique key number into file 
    		BufferedWriter out=new BufferedWriter(fwriter);
    		
    		out.write(""+currentUniqueKey);
    		out.close();
    		
    	}
    	catch (Exception e) {
    		System.out.println("Exception at: "+e);
    	}
    }//updateUniqueKey()
    
    /* addToFile() 
	 * This method adds new books created into save file library.txt
	 * Pre: A new book entry 
	 * Post: Added to library.txt
	 */
    
    private static void addToFile(String bookRecord){
    	int currentUniqueKey=0;
    	if (MyLibraryManager_Library.data.size()>1) {	//if theres more than one data in file 
    		getLastUniqueKey();
    		currentUniqueKey=lastUniqueKey+1;	//get the last unique key, and current is last + 1
    		updateUniqueKey(currentUniqueKey);
    	}
    	else if (MyLibraryManager_Library.data.size()==1) {	//if there is only one data in file
    		getLastUniqueKey();
    		currentUniqueKey=lastUniqueKey+1; 	//add the last unique key with 1 to get current unique key
    		updateUniqueKey(currentUniqueKey);
    	}
    	try {			
    		if (MyLibraryManager_Library.data.size()>1) {
    			FileWriter fwriter = new FileWriter ("library.txt",true); //append if data size is bigger than 1 
    			BufferedWriter out = new BufferedWriter (fwriter);
				out.write(currentUniqueKey+"|");	//write the current unique key 
				out.write(bookRecord);	//write the record
				out.newLine();
				out.newLine();
				out.close();
    		}
    		else if (MyLibraryManager_Library.data.size()==1) {
    			FileWriter fwriter = new FileWriter ("library.txt"); 	//overwrite if data size is equal to 1 
    			BufferedWriter out = new BufferedWriter (fwriter);
				out.write("Unique|Title|Author|Year|Pages|Publisher|Category|Rating|");	//rewrite heading
				out.newLine();
				out.newLine();
				out.write(currentUniqueKey+"|");	//write the current unique key
				out.write(bookRecord);				//write the record 
				out.newLine();
				out.newLine();
				out.close();
    		}
		}

		catch (Exception e) {
			System.out.println("Exception at "+e);
		}		
    }//addToFile()
    
    /* createFunction() 
	 * Initiates the Create Entries window when administrator clicks 'Create'
	 * Pre: User clicks Create
	 * Post: Opens up Create Entries window
	 */
    
    private class createFunction implements ActionListener {
		public void actionPerformed(ActionEvent e)
		{
			createWindow(frame);	//initate create entry window 
		}
	}//createFunction()
       
    /* deleteFunction() 
	 * Initiates the deleting functions when administrator clicks 'Delete'
	 * Pre: User clicks Delete
	 * Post: Initiates deleting functions
	 */
    
    private class deleteFunction implements ActionListener {
		public void actionPerformed(ActionEvent e)
		{			
	        	if (rowIndex==-1) {		//if no row is selected, error message will be outputted
	        		JOptionPane.showMessageDialog(frame,
							"You have not selected a row to delete.",
							"Error Message",
							JOptionPane.ERROR_MESSAGE);
	        	}
	        	else {
	        		if (MyLibraryManager_Library.data.size()>8) {	//remove the element from library
		        		for (int i=0; i<8; i++) {
		        			MyLibraryManager_Library.data.removeElementAt(8*rowIndex);
		        		}		        		
		        		updateFile(MyLibraryManager_Library.data);		//update library.txt save file 
	        		}
	        		else if (MyLibraryManager_Library.data.size()==8) {
	        			for (int i=0; i<7; i++) {
		        			MyLibraryManager_Library.data.removeElementAt(rowIndex+1);	//remove everything in the last line but the unique key
		        		}
	        			updateFile(MyLibraryManager_Library.data);	//save the unique key into file         			
	        			MyLibraryManager_Library.data.removeElementAt(0);	//then remove everything
	        		}
	        		
	        		dataChanged=true;	//notify table model of changes, update interface data
					MyLibraryManager_Library model=new MyLibraryManager_Library();	
					table.setModel(model);
					setUpYearColumn(table, table.getColumnModel().getColumn(3));		//make sure the drop-down lists are still available 
			        setUpPageColumn(table, table.getColumnModel().getColumn(4));
			        setUpCategoryColumn(table, table.getColumnModel().getColumn(6));
			        setUpRatingColumn(table, table.getColumnModel().getColumn(7));
	        	}
		}
	}//deleteFunction()
    
    /* deleteAllFunction() 
	 * Initiates the Create Entries window when administrator clicks 'Delete All'
	 * Pre: User clicks Delete All
	 * Post: Clears the save file, updates the database
	 */
    
    private class deleteAllFunction implements ActionListener {
		public void actionPerformed(ActionEvent e)
		{
			JOptionPane.showMessageDialog(frame,
            "All data has been deleted.");
			
			try {
				String lastUniqueKey;	//overwrites all data in in library.txt with blank
				lastUniqueKey= (String)MyLibraryManager_Library.data.elementAt(MyLibraryManager_Library.data.size()-8);
				FileWriter fwriter = new FileWriter ("library.txt"); 
				BufferedWriter out = new BufferedWriter (fwriter);
				out.write("Unique|Title|Author|Year|Pages|Publisher|Category|Rating|");
				out.newLine();
				out.newLine();
				out.write(lastUniqueKey);
				out.close();
			}

			catch (Exception e1) {
				System.out.println("Exception at "+e1);
			}
			MyLibraryManager_Library model=new MyLibraryManager_Library(); 
			table.setModel(model);
		}
	}//deleteAllFunction()
    
    private class sortFunction implements ActionListener {
    	public void actionPerformed (ActionEvent e)
    	{
    		MyLibraryManager_Library model=new MyLibraryManager_Library(); 
    		table.setModel(model);
    		MyLibraryManager_Sort sort=new MyLibraryManager_Sort();		//initiate sort function 
    	}
    }
    
    /* checkAccountFunction() 
	 * Initiates the Create Entries window when administrator clicks 'Check Account'
	 * Pre: User clicks Check Account
	 * Post: Opens up check window
	 */
    
    private class checkAccountFunction implements ActionListener {
		public void actionPerformed(ActionEvent e)
		{
			MyLibraryManager_CheckAccounts checkAccounts=new MyLibraryManager_CheckAccounts();		//initiate check accoun function 
		}
	}//checkAccountFunction()
    
    /* searchFunction() 
	 * Initiates the Create Entries window when administrator clicks 'Search'
	 * Pre: User clicks Search
	 * Post: Opens up search window
	 */
    
    private class searchFunction implements ActionListener {
		public void actionPerformed(ActionEvent e)
		{
			MyLibraryManager_Search search=new MyLibraryManager_Search(); 	//initiate search function 
		}
	}//searchFunction()
    
    /* logOutFunction() 
	 * Initiates the termination of program when administrator clicks 'Log Out'
	 * Pre: User clicks Log Account
	 * Post: Program terminates
	 */
    
    private class logOutFunction implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{   			 	
			frame.setVisible(false);		//close window 
			JOptionPane.showMessageDialog(frame,	//notify user that program is closed 
            "You are now logged out. Thank you for using MyLibraryManager!"); 	
			System.exit(0);		//terminate program
		}
	}//logOutFunction()
}