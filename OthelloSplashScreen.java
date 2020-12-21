package othello;

/**
 * File Name:       OthelloSplashScreen.java
 * Author:         	Philip Thesen, ID# 040797646 & Josef Kundinger-Markhauser, ID# 040824003 
 * Course:          CST8221 - JAP, Lab Section: 302
 * Assignment:      2, Part 2
 * Date:            December 9 2020
 * Professor:       Daniel Cormier
 * Purpose:         This is the class that is responsible for displaying a splash
					screen before opening the program.
 * Class list:      OthelloSplashScreen              
 */

import java.awt.*;
import java.net.URL;
import javax.swing.*;

/**
 * This class is responsible for launching the Othello splash screen.
 * 
 * @author Philip Thesen
 * @author Josef Kundinger-Markhauser
 * @version 1.0
 * @see othello
 * @since 1.8.0_191
*/
public class OthelloSplashScreen extends JWindow{

	/** The duration for the splash screen, in milliseconds. Value: @{value} */
	private final int duration;

	/**
	 * The default constructor to set the duration of the splash screen
	 * @param duration - The time the splash screen is displayed
	 */
	public OthelloSplashScreen(int duration) {
		this.duration = duration;
	}
	
	/**
	* Displays a splash screen to the user that is in the center of the screen.
	* The amount of time it is displayed is determined by the class constructor.
	*/
	public void showSplashWindow() {

		// Creates the main JPanel for the Othello game. 
		JPanel content = new JPanel(new BorderLayout()); 
		
		// Sets the parameters for the window and centers it in the middle of the screen. 
		int width = 1010 + 10;
		int height = 620 + 10;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		
		// Creates the splash screen with a picture and student information at the bottom 
		JLabel label = new JLabel(new ImageIcon(getClass().getResource("splash.gif")));
		JLabel names = new JLabel("Josef Kundinger-Markhauser 040824003 | Philip Thesen 040797646", JLabel.CENTER);
		names.setForeground(Color.white);
		// create custom RGB color 
		Color color = new Color(44, 127, 211); 
		
		// set the location and the size of the window 
		setBounds(x, y, width, height); 
		
		// Sets the colour, font, and layout position 
		content.setBackground(Color.RED);
		names.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		content.add(label, BorderLayout.CENTER);
		content.add(names, BorderLayout.SOUTH);
	    content.setBorder(BorderFactory.createLineBorder(Color.lightGray, 10));
		
	    setContentPane(content);	
		setVisible(true);  
		
		// Idles the splash screen while the program loads, for the duration amount, and gets rid of it when time is up 
		try {
			Thread.sleep(duration);
		} catch(InterruptedException e) {
			// destroy the window and release all resources 
		}
		dispose();
	}
}