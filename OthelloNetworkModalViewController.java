package othello;

/**
 * File Name:       OthelloViewController.java
 * Author:         	Philip Thesen, ID# 040797646 & Josef Kundinger-Markhauser, ID# 040824003 
 * Course:          CST8221 - JAP, Lab Section: 302
 * Assignment:      2, Part 2
 * Date:            December 9 2020
 * Professor:       Daniel Cormier
 * Purpose:         This is the class that is responsible for handling
                    the network aspect of the Othello GUI and functionality.
 * Class list:      OthelloNetworkModalViewController, Controller                
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * This class is responsible constructing the JDialog for the Network menu item.
 * 
 * @author Philip Thesen
 * @author Josef Kundinger-Markhauser
 * @version 1.0
 * @see othello
 * @since 1.8.0_191
*/
public class OthelloNetworkModalViewController extends JDialog
{

    /** Whether the user pressed the Connect button. */
    private Boolean hasConnected=false;
    
    /** A reference to the private inner Controller class for use by the two buttons. */
    private Controller handler = new Controller();
    
    /** The CombobBox you will need to create.*/
    //NOTE:  You're free to rename it, but as there are some references to it in this stub,
    //you'll need to be consistent when renaming them all.
    private JComboBox<String> portInput;
    
    /** The text field where the user will enter the hostname to connect to.*/
    //As above, you're free to rename this.  But be careful.
    private JTextField addressInput;

    /** The text field where the user will enter their name .*/
    private JTextField nameInput;

    private JLabel status ;

    /** String array that holds the default ports. */
    private String[] examplePorts = new String[] {null, "31000", "41000", "51000"};

   /**
    * The default constructor that creates Network modal.
    *
    * @param mainView - the main frame for the the modal.
    */
    public OthelloNetworkModalViewController (JFrame mainView)
    {
        //In Swing, it's ideal if we provide reference frame this will sit atop.
        //The title isn't relevant since we want this to be an undecorated dialog.
        super(mainView,"Enter Network Address",true);
        
        setUndecorated(true); 
        getRootPane().setBorder( BorderFactory.createMatteBorder(5, 5, 5, 5, Color.GRAY) );
        
        //Panels for the network layout
        Container networkPanel = getContentPane();
        JPanel addressPanel = new JPanel();
        JPanel portPanel = new JPanel();
        JPanel namePanel = new JPanel();
        JPanel statusPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        //Setting the layouts for each panel.
        networkPanel.setLayout(new GridLayout(5,1));
        addressPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        portPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,5,0));

        namePanel.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));

        //addresspanel
        addressInput = new JTextField(20);
        JLabel address = new JLabel("Address: ");
        address.setDisplayedMnemonic(KeyEvent.VK_A); 
        address.setLabelFor(addressInput);
        addressPanel.add(address);
        addressPanel.add(addressInput);
        
        //Port panel
        portInput = new JComboBox<String>(examplePorts);
        portInput.setEditable(true);
        JLabel port = new JLabel("Port: ");
        port.setDisplayedMnemonic(KeyEvent.VK_P); 
        port.setLabelFor(portInput);
        port.setBorder(BorderFactory.createEmptyBorder(0,0,0,23));
        portPanel.add(port);
        portPanel.add(portInput);

        //Name panel
        nameInput = new JTextField(20);
        JLabel name = new JLabel("Name: ");
        name.setLabelFor(nameInput);
        name.setBorder(BorderFactory.createEmptyBorder(0,0,0,14));
        namePanel.add(name);
        namePanel.add(nameInput);
        
        //status panel        
         status = new JLabel("");
        statusPanel.add(status);
        
        //button panel
        JButton connect = new JButton("Connect");
        JButton cancel = new JButton("Cancel");

        connect.addActionListener(handler);
        connect.setActionCommand("C");
        cancel.addActionListener(handler);

        buttonPanel.add(connect);
        buttonPanel.add(cancel);

        // add all to main panel
        networkPanel.add(addressPanel);
        networkPanel.add(portPanel);
        networkPanel.add(namePanel);
        networkPanel.add(statusPanel);
        networkPanel.add(buttonPanel);
    
        networkPanel.setPreferredSize(new Dimension(300,150));

        pack();
    }

    /** This method returns the value the user has entered.
        @return The actual value, unless there was an error or the user pressed the cancel button.
    */
    public String getAddress()
    {
        if (hasConnected)
        {
            return (addressInput.getText());
        }
        else
        {
            //You can return whatever error message you like.  This isn't cast in stone.
            return ("Error:  Invalid Address or attempt cancelled.");
        }
    }
    
    /** This method returns the value the user has entered.
        @return The actual value, unless there was an error or the user pressed the cancel button.
    */
    public String getName()
    {
        if (hasConnected)
        {
            return (nameInput.getText());
        }
        else
        {
            //You can return whatever error message you like.  This isn't cast in stone.
            return ("Error:  Invalid Address or attempt cancelled.");
        }
    }

    /** This method returns the port the user has selected OR ENTERED in the Combo Box.
    @return The port selected.  Returns -1 on invalid port or cancel pressed.
    */
    public int getPort()
    {
        int portnum;
        if (hasConnected)
        {
            //Ensure the user has entered digits.
            //You should probably also ensure the port numbers are in the correct range.
            //I did not.  That's from 0 to 65535, by the way.  Treat it like invalid input.
            try
            {
                portnum = Integer.parseInt((String)portInput.getSelectedItem());
            }
                catch(NumberFormatException nfe)
            {
                //I've been using a negative portnum as an error state in my main code.
                portnum = -1;
            }

            return portnum;
        }
        return -1;
    }
    
    /** Responsible for final cleanup and hiding the modal. Does not do much at the moment.*/
    public void hideModal()
    {
        //Add any code that you may want to do after the user input has been processed
        //and you're figuratively closing up the shop.
        setVisible(false);
        
    }
    
    /** Returns whether or not the user had pressed connect.
    @return True if the user pressed Connect, false if the user backed out with cancel.
    */
    public boolean pressedConnect()
    {
        return hasConnected;
    }
    
    /** The Controller for managing user input in the network dialogue.
    @author Daniel Cormier
    @version 1.3
    @since 1.8.0_261
    @see OthelloViewController
    */
    private class Controller implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            String s = evt.getActionCommand();
            String addressText = addressInput.getText();
            String nameText = nameInput.getText();
            boolean validInput = true;
            int portNum ;
            
            //I set the action command on my connect button to "C".
            if ("C".equals(s))
            {
                
                //In Assignment 2-2, we will be making revisions here.
                //This would be a great place to update the "Status" portion of the UI.

                if (addressText.equals("")){
                    status.setText("Error: Address must not be blank.");
                    validInput = false;
                }

                if (nameText.length()<3){
                    status.setText("Error: Name must be at least three characters long.");
                    validInput = false;
                }    

           
                try{
                    portNum = Integer.parseInt((String)portInput.getSelectedItem());
                    if (portNum < 0 || portNum > 65535){
                        status.setText("Error: Valid port ranges are from 0 to 65535");
                        validInput = false;
                    }

                } catch(NumberFormatException nfe){
                    status.setText("Error: Enter an integer for the port number");
                    validInput = false;
                }

                if(validInput == false){
                    hasConnected =false;
                }else{
                    hasConnected=true;
                }
                
                 if(hasConnected==true){
                     status.setText("Connected");
                     hideModal();
                 }
            }
            else //My "Cancel" button has an action command of "X" and gets called here.
            {
                hasConnected=false;
                hideModal();
            }
   
        }
        
    }
}
        

        

