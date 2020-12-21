package othello;

/**
 * File Name:       OthelloViewController.java
 * Author:         	Philip Thesen, ID# 040797646 & Josef Kundinger-Markhauser, ID# 040824003 
 * Course:          CST8221 - JAP, Lab Section: 302
 * Assignment:      2, Part 1
 * Date:            November 22, 2020
 * Professor:       Daniel Cormier
 * Purpose:         This is the class that is responsible for building
                    the GUI for the Othello game.
 * Class list:      OthelloViewController, Controller                
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.EventQueue;
import java.util.*;

/**
 * This class is responsible constructing the Othello game GUI.
 * 
 * @author Philip Thesen
 * @author Josef Kundinger-Markhauser
 * @version 1.0
 * @see othello
 * @since 1.8.0_191
*/
public class OthelloViewController extends JFrame{

    /** The main JPanel for the Othello GUI. */
    private JPanel main;

     /** 2D array that holds the JLabels for the game board. */
    private JLabel [][] labels     = new JLabel[8][8];

     /** Array that holds the button for the game board. */
    private JButton [] buttons     = new JButton[17];

     /** Instance of the inner Controller class. */
    private Controller control     = new Controller();

     /** Variable to hold the color for black game board squares. */
    private Color black            = Color.BLACK;

     /** Variable to hold the color for white game board squares.*/
    private Color white            = Color.WHITE;

     /** Variable to hold the color gray. */
    private Color gray             = Color.GRAY;

     /** Variable to hold the color light-gray. */
    private Color lightGray        = Color.lightGray;

     /** Variable to hold the color red. */
    private Color red              = Color.RED;
    
     /** Image Icon representing player 1 - the black game piece.*/
    private ImageIcon onePieceIcon = new ImageIcon(getClass().getResource("black_s.png"));

     /** Image Icon representing player 2 - the white game piece. */
    private ImageIcon twoPieceIcon = new ImageIcon(getClass().getResource("white_s.png"));
     
     /** Image for valid moves */
    private ImageIcon checkMark    = new ImageIcon(getClass().getResource("checkmark.png"));

     /** Represents the current piece total for player 1. */
    private int player1Pieces      = 2;

     /** Label that holds the number of peices for player 1 */
    private JLabel pl1Piece        = new JLabel();

     /** Represents the current piece total for player 2. */
    private int player2Pieces      = 2;

     /** Label that holds the number of peices for player 2 */
    private JLabel pl2Piece        = new JLabel();

    /** The text field for the user to enter a move*/
    JTextField userTextField       = new JTextField(10);

     /** The Instance of OthelloModel */
    private OthelloModel model     = new OthelloModel();

     /** Holds the values of who's turn it is */
    private int playersTurn        = 1;

     /** Boolean used to update the shown moves */
    private boolean showValidMoves = false;

     /** Keeps track of the number of skips in a row */
    private int numSkips           = 0;

    /**Pink center text area used to display what's happening */
    JTextArea rightCenter          = new JTextArea();

    /**Holds the gameMode number */
    int gameMode                   = 0;

    /**Network connect model */
    OthelloNetworkModalViewController networkModel;

    /**Network client controller */
    OthelloNetworkController client;

    /**Disconnect menu item */
    JMenuItem disconnect;

    /**Submit text button */
    JButton submitBtn;

    /**Connect menu item */
    JMenuItem connect;

    /**
    * The default constructor that creates a main JPanel 
    * it adds the components to it and constructs the Othello GUI.
    * It also sets a new game up on the board.
    */
    public OthelloViewController(){
        
        // Create Main panel and add the boarders.
        main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, gray));

        //Add sections to the main panel.
        main.add(createBoard(), BorderLayout.CENTER);
        main.add(createRightSide(), BorderLayout.EAST); 
        main.add(bottomInput(), BorderLayout.SOUTH);
        newGame();
   
        //Add main content panel.
        this.setContentPane(main);
        this.setTitle("Joey and Phil's Othello Client");
        this.setJMenuBar(menuBar());
        this.setMinimumSize(new Dimension(1010, 620));  
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        
        //Add window listener to allow userTextField to be the primary focus.
        this.addWindowFocusListener(new WindowAdapter()
        {
            /**
            * This function request for focus the JText field in the window.
            *
            *@param e - Window event.
            */
            public void windowGainedFocus(WindowEvent e){
                userTextField.requestFocusInWindow();
            }
        });
    }

    /**
    * This method resets the game board and information for new game based off the user selected debug scenario.
    */
    private void newGame(){
        playersTurn = 1;
        numSkips = 0;
        
        model.initialize(gameMode);
        updateGame();
        buttons[16].setEnabled(true);

        String gameText =  "Player 1 initialized with " + player1Pieces+ " piece(s).\nPlayer 2 initialized with " +player2Pieces+ " piece(s).";
        rightCenter.setText(gameText);
    }

    /**
    * A factory method that creates the JButtons for the Othello GUI.
    * 
    * @param text The button text
    * @param ac The action command for the button
    * @param fgc The foreground color for the button
    * @param bgc The background color for the button
    * @param handler The handler for the button.
    * @return JButton button - a JButton created by this method.
    */
    private JButton createButton(String text, String ac, Color fgc, Color bgc, Controller handler){
        JButton button = new JButton(text);
        button.setForeground(fgc);
        button.setBackground(bgc);
        button.addActionListener(handler);
        button.setActionCommand(ac);
        button.setPreferredSize(new Dimension(60,60));
        return button;
    }

    /**
    * A factory method that creates the JLabels for the playing board of Othello GUI.
    * It constructs either a white or black board square.
    *
    * @param color - Either black or white for the game board.
    * @return JLabel temp - a JLabel used as a game board square.
    */
    private JLabel createLabel(Color color){
       
        JLabel temp = new JLabel("");
        temp.setOpaque(true);
        temp.setBackground(color);
        temp.setMinimumSize(new Dimension(60, 60));
        temp.setPreferredSize(new Dimension(60, 60));
        temp.setMaximumSize(new Dimension(60, 60));

        return temp;
    }

    /**
    * A factory method that creates the JMenuItems for the Othello menu bar.
    * 
    * @param text The Menu text.
    * @param ac The action command for the menu item.
    * @param handler The handler for the menu item.
    * @return JMenuItem menuItem - a JMenuItem created by this method.
    */
    private JMenuItem createMenuItem(String text, String ac, Controller handler){
       
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(handler);
        menuItem.setActionCommand(ac);
        return menuItem;
    }

    /**
    * A factory method that creates the JRadioButtonMenuItems for the Othello menu bar.
    * 
    * @param text The JRadioButtonMenuItem text.
    * @param value The JRadioButtonMenuItem value.
    * @param ac The action command for the JRadioButtonMenuItems.
    * @param handler The handler for the JRadioButtonMenuItems.
    * @return JRadioButtonMenuItem radioItem - a JRadioButtonMenuItems created by this method.
    */
    private JRadioButtonMenuItem createRadioMenuItem(String text,boolean value, String ac, Controller handler){
       
        JRadioButtonMenuItem radioItem = new JRadioButtonMenuItem(text,value);
        radioItem.addActionListener(handler);
        radioItem.setActionCommand(ac);
        return radioItem;
    }

    /**
    * A method that creates the playing board and buttons for the Othello GUI.
    * It constructs the checkered board and the buttons on the bottom and right.
    *
    * @return JPanel board - a JPanel that consists of board and buttons.
    */
    private JPanel createBoard(){
        boolean isWhite = true; // to determine if the next square will be white or black
        int counter = 0;        // used to add the buttons into the panel
        char letter = 'A';      // text for the buttons.

        // Create a panel and set the layout.
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(9,9));
        board.setBorder(BorderFactory.createMatteBorder(1, 0, 5, 5, gray));

        // Create an array of buttons.
        for(int i = 0; i < 17; i++){
            if (i < 8){
                String text = Integer.toString(i+1);
                buttons[i] = createButton(text, text, black, lightGray, control);
                buttons[i].setBorder(BorderFactory.createMatteBorder(0, 2, 1, 1, gray));
            }
            else if(i == 16){
                buttons[i] = createButton("move", "move", black, white, control);
                buttons[i].setFont(new Font("", Font.PLAIN, 10));
                buttons[i].setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, gray));
            }
            else{
                String text = Character.toString(letter++);
                buttons[i] = createButton(text, text, black, lightGray, control);
                buttons[i].setBorder(BorderFactory.createMatteBorder(2, 0, 0, 1, gray));
                
            }
        }

        // Add all labels and buttons to the panel
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if (j == 8 || i == 8){
                    board.add(buttons[counter++]);
                }
                else if(isWhite){
                    labels[i][j] = createLabel(Color.BLACK);
                    board.add(labels[i][j]);
                    if(j <= 6){
                        isWhite = false;
                    }
                }
                else{
                    labels[i][j] = createLabel(Color.WHITE);
                    board.add(labels[i][j]);
                    if (j <= 6){
                        isWhite = true;
                    }
                }
            }
        }

        return board;
    }

    /**
    * A method that creates the right side of the Othello GUI.
    * It constructs the checkbox at the right-top, the pink right-center JLabel, 
    * and the right-bottom player piece information.
    *
    * @return JPanel rightMain - a JPanel that consists of the right side of the GUI.
    */
    private JPanel createRightSide(){

        JPanel rightMain = new JPanel();
        String checkMoves = "Show Valid Moves";
        JCheckBox rightTopCheck = new JCheckBox(checkMoves); 
        JPanel rightTop = new JPanel(); 
        
        JPanel rightBottom = new JPanel();
        JLabel player1 = new JLabel("Player 1 Pieces:");
        JLabel player2 = new JLabel("Player 2 Pieces:");
        pl1Piece = new JLabel();
        pl2Piece = new JLabel();

        rightMain.setLayout(new BorderLayout());

        // NORTH panel - A check-button for available moves.
        rightTopCheck.setActionCommand(checkMoves);
        rightTopCheck.addActionListener(control);
        rightTop.setLayout(new BorderLayout());
        rightTop.add(rightTopCheck, BorderLayout.WEST);
        rightTop.setOpaque(true);
        rightTop.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, gray));

        // CENTER panel - Labels with text and pink background/
        JScrollPane areaScrollPane = new JScrollPane(rightCenter);
        areaScrollPane.setPreferredSize((new Dimension(450, 0)));
       
        rightCenter.setBackground(Color.pink);
        rightCenter.setEditable(false);
        rightCenter.setLineWrap(true);
        rightCenter.setOpaque(true); 
        rightCenter.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));     
        
        // Bottom panel- Player piece count, labels, and piece icons. 
        rightBottom.setLayout(new GridLayout(2,2));
        rightBottom.setBorder(BorderFactory.createMatteBorder(5, 0, 5, 0, gray));

        player1.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        player2.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        pl1Piece.setIcon(onePieceIcon);
        pl1Piece.setHorizontalAlignment(JLabel.RIGHT);
        pl1Piece.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));

        pl2Piece.setIcon(twoPieceIcon);
        pl2Piece.setHorizontalAlignment(JLabel.RIGHT);
        pl2Piece.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));

        rightBottom.add(player1);
        rightBottom.add(pl1Piece);
        rightBottom.add(player2);
        rightBottom.add(pl2Piece);
        
        // Adding all the components into the rightMain panel.
        rightMain.add(rightTop, BorderLayout.NORTH);
        rightMain.add(areaScrollPane, BorderLayout.CENTER);
        rightMain.add(rightBottom, BorderLayout.SOUTH);

        return rightMain;

    }

    /**
    * A method that creates the right side of the Othello GUI.
    * It constructs the checkbox at the right-top, the pink right-center JLabel, 
    * and the right-bottom player piece information.
    *
    * @return JPanel bottomPanel - a JPanel that consists of the right side of the GUI.
    */
    private JPanel bottomInput(){   

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(0, 26));
        
        //Text field for user input
        userTextField.setBackground(Color.white);
        userTextField.setHorizontalAlignment(JTextField.LEFT);
        userTextField.setEditable(true); 

        //Submit button
        submitBtn = createButton("Submit", "Submit", red, black, control);
        submitBtn.setFont(new Font("", Font.PLAIN, 10));
        submitBtn.setMargin(new Insets(0, 0, 0, 0));
        submitBtn.setEnabled(false);

        //Add each component to the bottomPanel
        bottomPanel.add(userTextField, BorderLayout.CENTER);
        bottomPanel.add(submitBtn, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
    * A method that creates the menu bar and it's menu items for the Othello GUI.
    *
    * @return JMenuBar menuBar - a JMenuBar that consists of menus and menu items.
    */
    private JMenuBar menuBar(){

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu =  new JMenu("File");
        JMenu gameMenu =  new JMenu("Game");
        JMenu netMenu  =  new JMenu("Network");
        JMenu helpMenu =  new JMenu("Help");

        // Network menu
        connect = createMenuItem("New Connection", "connect", control);
        disconnect = createMenuItem("Disconnect", "disconnect", control);
        netMenu.add(connect);
        netMenu.add(disconnect);
        disconnect.setEnabled(false);

        //File menuItems created and added to fileMenu
        JMenuItem fileNewGame = createMenuItem("New Game", "newGame", control);
        JMenuItem fileLoadGame = createMenuItem("Load", "saveGame",control); 
        fileLoadGame.setEnabled(false);
        JMenuItem fileSaveGame = createMenuItem("Save", "loadGame",control); 
        fileSaveGame.setEnabled(false);
        JMenuItem fileExit = createMenuItem("Exit", "Exit", control);

        fileMenu.add(fileNewGame);
        fileMenu.add(fileLoadGame);
        fileMenu.add(fileSaveGame);
        fileMenu.add(fileExit);

        //Game menuItems created and added to gameMenu
        JMenu subReskin = new JMenu ("Reskin Pieces");
        JMenu subDebug = new JMenu ("Debug Scenerios");
        ButtonGroup reskinBtnGroup = new ButtonGroup();
        ButtonGroup debugBtnGroup = new ButtonGroup();

        //Create the reskin sub menu items and add them to a button group.
        JRadioButtonMenuItem normal = createRadioMenuItem("Normal Pieces (black and white)", true, "normal" , control);
        JRadioButtonMenuItem catsDogs = createRadioMenuItem("Cats vs. Dogs", false, "catDog", control);
        JRadioButtonMenuItem pumpBats = createRadioMenuItem("Pumpkins vs. Bats", false, "pumpBat" ,control);
        reskinBtnGroup.add(normal);
        reskinBtnGroup.add(catsDogs);
        reskinBtnGroup.add(pumpBats);

        //Add to the reskin submenu.
        subReskin.add(normal);
        subReskin.add(catsDogs);
        subReskin.add(pumpBats);

        //Create the debug scenario sub menu items and add them to a button group.
        JRadioButtonMenuItem normalGamne = createRadioMenuItem("Normal Game", true, "zero" , control);
        JRadioButtonMenuItem cornerTest = createRadioMenuItem("Corner Test", false, "one", control);
        JRadioButtonMenuItem sideTest = createRadioMenuItem("Side Test", false, "two" ,control);
        JRadioButtonMenuItem oneX = createRadioMenuItem("1 X Capture Test", false, "three" , control);
        JRadioButtonMenuItem twoX = createRadioMenuItem("2 X Capture Test", false, "four", control);
        JRadioButtonMenuItem emptyBoard = createRadioMenuItem("Empty Board", false, "five" ,control);
        JRadioButtonMenuItem innerSquare = createRadioMenuItem("Inner Square Test", false, "six" ,control);
        debugBtnGroup.add(normalGamne);
        debugBtnGroup.add(cornerTest);
        debugBtnGroup.add(sideTest);
        debugBtnGroup.add(oneX);
        debugBtnGroup.add(twoX);
        debugBtnGroup.add(emptyBoard);
        debugBtnGroup.add(innerSquare);

        //Add to the Debug Scenario submenu.
        subDebug.add(normalGamne);
        subDebug.add(cornerTest);
        subDebug.add(sideTest);
        subDebug.add(oneX);
        subDebug.add(twoX);
        subDebug.add(emptyBoard);
        subDebug.add(innerSquare);
      
        gameMenu.add(subReskin);
        gameMenu.add(subDebug);

         //Help menuItem created and added to helpMenu
        JMenuItem helpAbout =  createMenuItem("About", "aboutInfo", control); 
        helpMenu.add(helpAbout);

        //Add each component to the menuBar before returning it
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(netMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
    * This method updates the game pieces and chip count for the game. 
    */
    private void updateGame(){

        //Update the board peices.
        displayPieces();     

        //Check to make sure there is a valid move for the next player.
        checkIfPlayerCanMove();

        //Update the number of chip count.
        player1Pieces = model.getChips(1);
        player2Pieces = model.getChips(2);
        pl1Piece.setText(Integer.toString(player1Pieces));
        pl2Piece.setText(Integer.toString(player2Pieces));

    }

    /**
    * This method updates the 'move' button to 'skip' if player cannot move. Also signals end of game if condition is triggered.
    */
    private void checkIfPlayerCanMove(){
        if(!(model.canMove(playersTurn))){
            buttons[16].setText("skip");
            buttons[16].setActionCommand("skip");
            numSkips++;
            if (numSkips >= 2){
                endGame();
            }
            else{
                rightCenter.append("\nPlayer " + playersTurn + " has no valid moves. Press skip.");
            }
        }else{
            buttons[16].setText("move");
            buttons[16].setActionCommand("move");
            numSkips = 0;
        }
    }

    /**
    * This method displays valid moves on the game board, to the current player.
    */
    private void showValidMoves(){
        //Show the valid moves if there are any
        if (showValidMoves){
            for(int x = 0; x < 8; x++){
                for(int y = 0; y < 8; y++){
                    if(model.isValid(x,y,playersTurn)){
                        labels[x][y].setIcon(checkMark);
                    }
                }
            }
        }
        else{
            for(int x = 0; x < 8; x++){
                for(int y = 0; y < 8; y++){
                    if(model.isValid(x,y,playersTurn)){
                        labels[x][y].setIcon(null);
                    }
                }
            }
        }
    }

    /**
    * This method displays the game pieces  on the game board.
    */
    private void displayPieces(){
        int temp;

        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                temp = model.getBoard(x,y);
                if (temp == 1){
                    labels[x][y].setIcon(onePieceIcon);
                    labels[x][y].setHorizontalAlignment(JLabel.CENTER);
                }
                else if(temp == 2){
                    labels[x][y].setIcon(twoPieceIcon);
                    labels[x][y].setHorizontalAlignment(JLabel.CENTER);
                }
                else{
                    labels[x][y].setIcon(null);
                }
            }
        }
        showValidMoves();
    }

    /**
    * This method sets the current turn to the active player.
    */
    private void changeTurn(){
        if(playersTurn == 1){
            playersTurn = 2;
        }
        else{
            playersTurn = 1;
        }  
    }

    /**
    * This method displays game information in the text field based on the end of game results.
    */
    private void endGame(){

        rightCenter.append("\nPlayer 1 finishes with " + player1Pieces + " chips.");
        rightCenter.append("\nPlayer 2 finishes with " + player2Pieces + " chips.");
        
        if (player1Pieces > player2Pieces){
            rightCenter.append("\nPlayer 1 WINS!!");
        }
        else if(player2Pieces > player1Pieces){
            rightCenter.append("\nPlayer 2 WINS!!");
        }
        else{
            rightCenter.append("\nIt is a Tie, Try to be better next time!");
        }
        buttons[16].setEnabled(false);

        rightCenter.append("\n\nPlay again? (Select 'NEW GAME' from the File menu.)");
        
    }

    /**
    * This method updates the game mode based on the debug scenario selected by the user.
    *
    * @param arg - The action command passed in.
    */
    public void changeGameMode(String arg){
        switch(arg){
            case("zero"):
                gameMode =0;
                break;
            case("one"):
                gameMode=1;        
                break;
            case("two"):
                gameMode=2;                 
                break;
            case("three"):
                gameMode=3;                
                break;
            case("four"):
                gameMode=4;              
                break;
                case("five"):
                gameMode=5;;              
                break;
            case("six"):
                gameMode=6;                  
                break;
            default:
                break;
        }
    }

    /**
    * This method updates the 'skin' of the game pieces based on the user's selection.
    *
    *@param piece1 - The new 'skin' of player 1's piece.
    *@param piece2 - The new 'skin' of player 2's piece.
    */
    public void reskin(String piece1, String piece2){

        onePieceIcon = new ImageIcon(getClass().getResource(piece1));
        twoPieceIcon = new ImageIcon(getClass().getResource(piece2));

        pl1Piece.setIcon(onePieceIcon);
        pl1Piece.setHorizontalAlignment(JLabel.RIGHT);
        pl2Piece.setIcon(twoPieceIcon);
        pl2Piece.setHorizontalAlignment(JLabel.RIGHT);

        displayPieces();  
    }

    /**
    * This method creates the network model and sets the location on screen.
    */
    public void createNetworkConnection(){
        networkModel = new OthelloNetworkModalViewController(this);

        Point thisLocation = getLocation();
        Dimension parentSize = getSize();
        Dimension dialogSize = networkModel.getSize();

        int offsetX = (parentSize.width-dialogSize.width)/2+thisLocation.x; 
        int offsetY = (parentSize.height-dialogSize.height)/2+thisLocation.y;

        networkModel.setLocation(offsetX,offsetY); 
        networkModel.setVisible(true);

    }

    /**
     * This method attempts to connect to the server.
     */
    public void connect(){
       
        String address = networkModel.getAddress();
        int port = networkModel.getPort();
        String name = networkModel.getName();

        rightCenter.append("\nNegotiating connection with "+ address + " on port "+ port);
       
        client = new OthelloNetworkController(address, port, name, this);
        boolean ret = client.connected();
        if (ret){
            rightCenter.append("\nConnection Successful");
            client.start();
            disconnect.setEnabled(true);
            submitBtn.setEnabled(true);
        }
        else{
            rightCenter.append("\nConnection not successfull");
        }
    }

    /**
    * A private inner class that reads action listeners from the user front end and performs actions based on certain actions.
    * 
    * @author Philip Thesen
    * @author Josef Kundinger-Markhauser
    * @version 1.0
    * @see othello
    * @since 1.8.0_191
    */
    private class Controller implements ActionListener{

        /** Default X and Y values for the board coordinates. */
        int x = -1, y = -1;

        /**
		 * This method reads action commands and initiates certain functionalities
         * based on the action command read.
		 * 
		 * @param e - to display action of the button pressed.   
		 */
        @Override
        public void actionPerformed(ActionEvent e){
            String arg = e.getActionCommand();
            //System.out.println(arg);
            char tempChar = 'A';
            
            //Determine if the x axis was selected.
            for(int i = 8; i < 16; i++){
                String temp = Character.toString(tempChar);
                if(temp.equals(arg)){
                    selectXButton(i);
                }
                tempChar++;
            }

            //Determine if the y axis was selected.
            for(int i = 0; i < 8; i++){
                String temp = Integer.toString(i+1);
                if (temp.equals(arg)){
                    selectYButton(i);
                }
            }

            //If move button is pressed.
            if("move".equals(arg)){
                if(x != -1 && y != -1){
                    int moveReturn = model.move(y,x,playersTurn);
                    if (moveReturn != 0){
                        rightCenter.append("\nPlayer " + playersTurn + " Captured " + moveReturn + " Chips");
                        changeTurn();
                        updateGame();
                        buttons[x + 8].setBackground(lightGray);
                        buttons[y].setBackground(lightGray);
                        x = -1;
                        y = -1;
                    }
                }
            }

            //If skip button is pressed.
            if("skip".equals(arg)){
                changeTurn();
                updateGame();
                if (x != -1){
                    buttons[x + 8].setBackground(lightGray);
                    x = -1;
                }
                if (y != -1){
                    buttons[y].setBackground(lightGray);
                    y = -1;
                }
            }

            //If show valid moves box is clicked.
            if("Show Valid Moves".equals(arg)){
                showValidMoves = !showValidMoves;
                showValidMoves();
            }

            //If exit is selected.
            if(e.getActionCommand().equals("Exit")) {
                dispose();
                System.exit(0);
            }

            //If the skin needs to be changed or the about button is selected.
            switch(arg){
                case("catDog"):
                    reskin("cat.png", "dog.png");
                    break;
                case("pumpBat"):
                    reskin("bat.png", "pumpkin.png");
                    break;
                case("normal"):
                    reskin("black_s.png", "white_s.png");
                    break;
                 case("aboutInfo"):
                    JOptionPane.showMessageDialog(null,
                    "Othello Game\nby Josef Kundinger-Markhauser\nand Phil Thesen\nNovember2020",
                    "About", 1);
                    break;
            }

            //If it is a network menu item
            switch(arg){
                case("connect"):
                    createNetworkConnection();

                    // If cancel was pressed.
                    if (networkModel.pressedConnect()){
                        connect();
                    }
                    break;
                case("disconnect"):
                    client.disconnect();
                    break;
            }

            //checks if the action command was one of the debug scenerios and sets the model.initialize
            changeGameMode(arg);
           
            //updates the board if newGame is selected.
            if ("newGame".equals(arg)){
                newGame();
            }

            if ("Submit".equals(arg)){
                String text = userTextField.getText();
                if (text != null){
                    client.toServer(text);
                }

                userTextField.setText("");
            }

        }

        /**
        * This method changes the selected X button to white when selected.
        *
        * @param temp - The button pressed.
        */
        public void selectXButton(int temp){
            if (x != -1){
                buttons[x + 8].setBackground(lightGray);
                buttons[temp].setBackground(white);
            }
            else{
                buttons[temp].setBackground(white);
            }
            x = temp - 8;
        }

        /**
        * This method changes the selected Y button to white when selected.
        *
        * @param temp - The button pressed.
        */
        public void selectYButton(int temp){
            if (y != -1){
                buttons[y].setBackground(lightGray);
                buttons[temp].setBackground(white);
            }
            else{
                buttons[temp].setBackground(white);
            }
            y = temp;
        }
    }

}

