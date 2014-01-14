package mylibrarymanager;

/*
 * Program Name: MyLibraryManager	Contributor: David Zhang 	Due Date: June 1, 2012
 * 
 * Program Description: This program simulates a library database for books, which contains two interfaces: One for administrator and one for
 * 						normal borrower. Log-in is required for all users, and new users can sign up for new account, while administrator can 
 * 						sign in directly using the user name 'admin' of any case. Once logged in, the user will gain access to the library, and
 * 						normal users only have the option of signing out/returning books, searching through the in-library books, checking their 
 * 						own account status, and logging out once finished. 
 * 
 * 						The administrator will have the priviledge to access anything within the library, including creating new books, deleting 
 * 						in-library books one at a time or all at once, editing in-library books, searching through 
 * 						in-library books, and access the account status of all users. He/she could then log out once finished.
 * 
 * 						The database could store an unlimited amount of books, and could display them through a JTable interface. No due 
 * 						dates are applied for books signed out, and the number of books the user could sign out is unlimited.
 *
 *						All Rights Reserved (C) 2012, David Zhang. 
 */

import javax.swing.*;

/*
 * This portion initiates the entire program by loading the GUI portion of the log-in method.
 */

public class MyLibraryManager {
	
	public static void main (String [] args) {
		JFrame f= new JFrame();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MyLibraryManager_LogIn.createAndShowGUI();	//Runs GUI portion of the log-in method 
			}
		});
	}
}
