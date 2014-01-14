package mylibrarymanager;

/*
 * TIME STAMP: June 1, 2012 - 
 * LAST EDITED/REVIEWED BY: David Zhang
 * REPORT: Everything good to go.
 */

import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

/*
 * This section is a model on which the JTable in the database is built upon. It includes all the critical 
 * methods that allow the JTable to manipulate its data and its cells.
 */

public class MyLibraryManager_Library extends AbstractTableModel{
	
	public static Vector data;	//very critical vector that stores all data within the table and is used among all the classes 
    Vector columns;	//stores all column information 	
    JFrame frame;
    private Object [][] values;	//only used within this class to process editing
    
    private String[] columnNames = {"Unique Key",	//set the column headers 
    		"Book Title",
    	    "Author Name",
    	    "Year Published",
    	    "Page Numbers",
    	    "Publisher",
    	    "Category",
    	    "Book Rating (/10)",
    	    };
    
    /* MyLibraryManager_Library() 
	 * This method reads and obtains all the values for the core vector data
	 * Pre: Reads from library.txt
	 * Post: Stores the data as vector into vector data
	 */
       
	public MyLibraryManager_Library() {
		
		String line;
		data= new Vector();
		columns= new Vector();
		
	    try {		//reads the library.txt file, which contains all the data for the library 
	            FileInputStream fis = new FileInputStream("library.txt");
	            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	            StringTokenizer st1 = new StringTokenizer(br.readLine(), "|");	//all data are stored using '|' as a separator 
	            while (st1.hasMoreTokens())
	                    columns.addElement(st1.nextToken()); // obtains the information read in first row as the column headers 
	            while ((line = br.readLine()) != null) {
	                    StringTokenizer st2 = new StringTokenizer(line, "|");
	                    while (st2.hasMoreTokens())
	                            data.addElement(st2.nextToken());	//obtains all remaining information as useful information for the table 
	            }
	            values=new Object[data.size()/8][8];
	            transferDataToValue(data);
	            br.close();} 
	    catch (Exception e) {	//tells the user in a user-friendly message if the 'library.txt' file is missing, and how to fix/add the file 
	    	System.out.println("Notice from MyLibraryManager: Please repair or add file 'library.txt' into the workspace folder " +
			"of MyLibraryManager to continue program operation.");
	    	System.out.println("Add the following line into 'library.txt': Unique|Title|Author|Year|Pages|Publisher|Category|Rating|");
	    	System.out.println("Then add two blank lines, and add a single integer which indicates the initial unique key number for the library");
	    	System.out.println("You may close the file.");
	    	System.exit(0);
	    }
	}//MyLibraryManager_Library()
	
	/* transferDataToValue() 
	 * This method transfers the vector data into an array for editing processing
	 * Pre: //
	 * Post: Transfers vector into an array
	 */
	
	private void transferDataToValue(Vector data) {	//transfers the vector data into the 2-d array for use in processing editing only 
		int k=0;
		for (int i=0; i<data.size()/8;i++) {
			for (int j=0; j<8;j++) {
				values[i][j]=data.elementAt(k);
				k++;
			}
		}
		
	}//transferDataToValue()
	
	/* setValueAt() 
	 * This method saves the edited value for cells immediately
	 * Pre: User changes the value of a cell
	 * Post: The value is saved on the interface and in library.txt
	 */
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {		
		values[rowIndex][columnIndex]=aValue;	//the values array is immediately changed if a change in a cell is detected 
		fireTableDataChanged();	//the table is notified of change, and immediately changes the interface
		data.set(rowIndex*8+columnIndex,((String) values[rowIndex][columnIndex]).trim());	//the core data is also changed immediately
		MyLibraryManager_Administrator.updateFile(data);	//the library.txt file is immediately updated 
	}//setValueAt()
		
	public int getRowCount() {	//get the number of rows depending on the size of data divided by number of columns 
	    return data.size() / getColumnCount();
	}
	
	public int getColumnCount() {	//get the number of columns 
	    return columns.size();
	}
	
	/* getValueAt() 
	 * This method gets the value of every cell 
	 * Pre: //
	 * Post: returns the most updated values for all cells
	 */
	
	public Object getValueAt(int rowIndex, int columnIndex) {	//get the value of each cell 
		if (MyLibraryManager_Administrator.dataChanged==false) { //if data is changed by admin, the table will be notified immediately 
			fireTableDataChanged();
			MyLibraryManager_Administrator.dataChanged=true; 
		}
		
		return (String) values[rowIndex][columnIndex];	//returns the value as an array
	}//getValueAt()
	
	public String getColumnName(int col) {	//gets column header names
		return columnNames[col];
	}
	
	/* isCellEditable() 
	 * This method determines which cells are editable and which are not 
	 * Pre: //
	 * Post: For administrator, all cells except the unique key column can be edited; otherwise, all cells cannot be edited 
	 */
	
	public boolean isCellEditable(int row, int col) {
		
		if (MyLibraryManager_LogIn.globalAccount.equalsIgnoreCase("Admin")) {	//only the first column is ineditable for admin
			if (col==0) return false;
			else return true; 	//everything else is editable 
		} else {	//normal user cannot edit any cell
		    return false;
		}
	}//isCellEditable()
	
	
}
