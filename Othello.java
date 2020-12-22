package othello;

/**
 * File Name:       Othello.java
 * Author:         	Philip Thesen, ID# 040797646 & Josef Kundinger-Markhauser, ID# 040824003 
 * Course:          CST8221 - JAP, Lab Section: 302
 * Assignment:      2, Part 2
 * Date:            December 9 2020
 * Professor:       Daniel Cormier
 * Purpose:         This is the class that is responsible for launching the program/GUI.
 * Class list:      Othello                
 */

import java.awt.*;
import java.net.URL;
import javax.swing.*;

/**
 * This class is responsible for launching the Othello game and contains the main method.
 * 
 * @author Philip Thesen
 * @author Josef Kundinger-Markhauser
 * @version 1.0
 * @see othello
 * @since 1.8.0_191
*/
public class Othello {

	/**
	 * Main method for the Othello game. It creates a splash screen for the application
     * and then constructors and displays the Othello game GUI.
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		
		/**An instance of the OthelloSplashScreen class */
		OthelloSplashScreen obj = new OthelloSplashScreen(5000);  

		//Displays the splash screen.
		obj.showSplashWindow();

		//Opens up the program and sets the window parameters and behaviours
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				/**An instance of the OthelloViewController class */
				OthelloViewController frame = new OthelloViewController();
			}
		});		
	}
}