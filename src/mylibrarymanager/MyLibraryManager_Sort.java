package mylibrarymanager;

/*
 * TIME STAMP: June 1, 2012 - 
 * LAST EDITED/REVIEWED BY: David Zhang
 * REPORT: Everything good to go.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/*
 * This section features the manual sorting function for a field in the database; Alternate sort could be done by clicking on the category buttons 
 * on the main interface window. 
 */

public class MyLibraryManager_Sort {
		
	private static JFrame frame;	//initiate frame and panel for GUI 
	private JPanel p;
	private JLabel sortLabel=new JLabel("Sort: ");
	private static String[] fieldStrings = {"Book Title","Author Name","Year Published","Page Numbers","Publisher","Category","Book Rating"};
																							//array used to store information in drop-down losts 
	private static int[] sortIndex;	//store indices in column for sorting
	private static int[] sortIndexOrganizer;		//stores and organizes sorting indices 
	private static String[] recordAt= new String [1000]; 
	private static int sortColumn=0;
	private static Vector sortResults=new Vector();
	
	private JComboBox fieldList;	//initiates drop-down lists 
	
	private JButton OK=new JButton("OK");	//initiates buttons 
	private JButton CANCEL=new JButton("Cancel");

	/* MyLibraryManager_Sort() 
	 * This method sets up the sort GUI interface 
	 * Pre: User logged in as administrator
	 * Post: The main administrator interface is created
	 */
	
	public MyLibraryManager_Sort() {	
		
			sortIndex=new int [MyLibraryManager_Library.data.size()/8];
			
			p = new JPanel();		//creates new panel with drop-down lists and buttons added 
			p.setLayout(new FlowLayout());
			sortLabel.setFont(new Font("Century Gothic",Font.BOLD, 14));
			
			OK.setFont(new Font("Century Gothic",Font.BOLD, 12));
			CANCEL.setFont(new Font("Century Gothic",Font.BOLD, 12));
				
			fieldList =  new JComboBox(fieldStrings);
			fieldList.setSelectedIndex(0);
			fieldList.setFont(new Font("Century Gothic",Font.BOLD,12));
			
			Box box = Box.createHorizontalBox();
            box.add(Box.createHorizontalGlue());
            box.add(sortLabel); box.add(fieldList); //add the box containing elements to the panel 
                       
            Box box2 = Box.createHorizontalBox();
            box2.add(Box.createHorizontalGlue());
            box2.add(OK); box2.add(CANCEL);
            
			p.add(box);
			p.add(box2);
						
			OK.addActionListener(new ActionListener() {	//action performed when user clicks OK 
				public void actionPerformed(ActionEvent e) {
					int counter=0;
					String sortBy=(String)fieldList.getSelectedItem();	//get the field to be sorted and the order it is to be sorted in 
					sortIndexOrganizer=new int[sortIndex.length];	
					
					frame.setVisible(false);
					
					if (MyLibraryManager_Library.data.size()==1) {
						JOptionPane.showMessageDialog(frame,
			                    "There are no records to sort.",
			                    "Error Message",
			                    JOptionPane.ERROR_MESSAGE);
					}
					
					else {
						
						Vector dataToBeSorted=new Vector();	//stores all the data within the sort column 

						for (int i=0; i<7; i++) {
							if (sortBy.equals(fieldStrings[i])) sortColumn=i+1;	//get the column number in order to find the appropriate sort data 								
						}
						
						for (int i=sortColumn; i<MyLibraryManager_Library.data.size(); i++) {
							dataToBeSorted.addElement(MyLibraryManager_Library.data.elementAt(i));	//adds all elements within the column to the vector
							sortIndex[counter]=i;
							i=i+7;
							counter++; 
						}
							
						for (int i=0; i<sortIndex.length; i++) {
							sortIndexOrganizer[i]=sortIndex[i];		//adds the sort indices to an organizer to process in sort 
						}
						
						for (int i=0; i<recordAt.length; i++) {
							recordAt[i]="";		//initialize the string display of sort results
						}
						
						for (int i=0; i<sortIndex.length;i++) {
							for (int j=sortIndex[i]/8*8; j<sortIndex[i]/8*8+8; j++) {
								recordAt[sortIndex[i]]=recordAt[sortIndex[i]]+MyLibraryManager_Library.data.elementAt(j)+"|";	//add element from
								//original sort indices into string display of results
							}							
						}
						sortFunction(dataToBeSorted, 0, dataToBeSorted.size()-1);	//initiate sort function of elements within the column
						
						System.out.println("Sort Results: "+sortBy);
						System.out.println("Printed on console in order to view full sort results list");
						System.out.println("");
						System.out.println("Unique Key|Book Title|Author Name|Year Published|Page Numbers|Publisher|Category|Book Rating");
						System.out.println("");
						
						for (int i=0; i<sortIndexOrganizer.length; i++) {
							sortResults.add(recordAt[sortIndexOrganizer[i]]);		//add the records that have the elements of the sorted sort indices
							//into a vector that will be displayed on the results window
							System.out.println(recordAt[sortIndexOrganizer[i]]);
						}
						System.out.println("_______");
						sortResultsWindow(sortBy);			//initiate GUI window showing results	
					}
				}
			});
			
			CANCEL.addActionListener(new ActionListener(){	//closes window if user presses cancel 
				public void actionPerformed(ActionEvent e){
					frame.setVisible(false);
				}
			});
				
			frame = new JFrame("Sort Book Entries");	//spawn frame and let it be visible 
			frame.setResizable(false);
			frame.add(p);
			frame.setSize(350,100);
			frame.setVisible(true);
	}
	
	/* sortFunction() 
	 * This method performs the sorting function of elements within a given column
	 * Pre: Initiated by MyLibraryManager_Sort()
	 * Post: The input vector data is sorted in order 
	 */
	
	private static void sortFunction(Vector data, int begin, int end) {	//the sorting algorithm used is Quick Sort, which sorts 
		int i=begin, j=end;														//according to a pivotal point among the data 
		String pivot= (String) data.elementAt(end), swap, swap2;		//the end of the data is considered as the pivotal point
		while (i <= j) {
			while (((String)data.elementAt(i)).compareToIgnoreCase(pivot)< 0) i++;	//adds the beginning counter if the beginning element is less than the pivot
			while (((String)data.elementAt(j)).compareToIgnoreCase(pivot)> 0) j--; 	//subtracts the ending coutner if the ending element is greater than the pivot
			if (i<=j) {	//if the first index is smaller than the ending index 
				if (i!=j) {
					int tempStorage=sortIndexOrganizer[j];
					swap=(String)data.elementAt(j);	//the two data swap with each other 
					swap2=(String)data.elementAt(i);
					data.set(i, swap);
					data.set(j,swap2);
					sortIndexOrganizer[j]=sortIndexOrganizer[i];	//the sorting indices are also swapped with each other 
					sortIndexOrganizer[i] = tempStorage;
				}
				i++; 
				j--; 
			}				
		}
		if (begin < j) {	//re-uses the function as necessary until the right order is sorted 
			sortFunction (data, begin, j);
		}
		if (i < end) {
			sortFunction (data, i , end);
		}
 	}//sortFunction()
	
	/* sortResultWindow() 
	 * This method sets up the sort results GUI interface
	 * Pre: Sorting is performed
	 * Post: GUI window is initiated to display the sorting results 
	 */
	
	private static void sortResultsWindow(String sortBy) {
		final JFrame frame2=new JFrame("Sort Results: "+sortBy);	//create frame, panel, labels, buttons, and allocate label space to store search results
		JPanel p=new JPanel();  
    	p.setBackground(Color.DARK_GRAY);
    	p.setLayout(new FlowLayout());
    	JLabel categories= new JLabel ("Unique Key | Book Title | Author Name | Year Published | Page Numbers | Publisher | Category | Book Rating");
    	categories.setFont(new Font("Century Gothic",Font.BOLD, 14));
    	categories.setForeground(Color.WHITE);
    	JLabel [] sortResultsLabel= new JLabel[sortResults.size()];
    	JButton OK=new JButton ("OK");	
    	JButton Back=new JButton("Back");
    	Box box = Box.createVerticalBox();
        box.add(Box.createVerticalGlue());
        box.add(categories);
        for (int i=0; i<sortResults.size(); i++) {        	
        	sortResultsLabel[i]=new JLabel(""+sortResults.elementAt(i));	//add vector elements into results label 
        	sortResultsLabel[i].setFont(new Font("Century Gothic", Font.BOLD, 16));
        	sortResultsLabel[i].setForeground(Color.WHITE);
        	box.add(sortResultsLabel[i]);	//add box containing search results 
        }
        sortResults.clear();
    	Box box2=Box.createHorizontalBox();
    	box2.add(Box.createHorizontalGlue());
    	box2.add(OK); box2.add(Back);		//add boxes containg all GUI items with search results 
    	p.add(box2);
    	p.add(box);
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
				frame.setVisible(true);
				frame2.setVisible(false);
			}
		});
	}//sortResultsWindow() 
}
