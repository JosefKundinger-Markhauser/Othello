package othello; 

/**
 * File Name:       OthelloModel.java
 * Author:         	Philip Thesen, ID# 040797646 & Josef Kundinger-Markhauser, ID# 040824003 
 * Course:          CST8221 - JAP, Lab Section: 302
 * Assignment:      2, Part 2
 * Date:            December 9 2020
 * Professor:       Daniel Cormier
 * Purpose:         This is the class that is responsible for the game logic that will be utilized
										in OthelloViewController.
 * Class list:      OthelloModel              
 */

import java.util.ArrayList;

/**
 * This class is responsible that holds the Othello game logic as well
 * as well as the debug scenarios.
 * 
 * @author Philip Thesen
 * @author Josef Kundinger-Markhauser
 * @version 1.0
 * @see othello
 * @since 1.8.0_191
*/
public class OthelloModel
{
	/** The game board as a 2D array. */
	private int[][] board=new int[7][7];

	/**  ArrayList used to check for valid moves*/
	private ArrayList<Integer> flagged = new ArrayList<Integer>();
	
	/** Represents a normal game scenario. Value: @{value} */
	public static final int NORMAL=0;

	/** Represents a corner test game scenario. Value: @{value} */
	public static final int CORNER_TEST=1;

	/** Represents a outer test game scenario. Value: @{value} */
	public static final int OUTER_TEST=2;

	/** Represents a 1 X capture game scenario. Value: @{value} */
    public static final int TEST_CAPTURE=3;

	/** Represents a 2 X capture game scenario. Value: @{value} */
	public static final int TEST_CAPTURE2=4;

	/** Represents a empty board game scenario. Value: @{value} */
	public static final int UNWINNABLE=5;

	/** Represents a inner test game scenario. Value: @{value} */
	public static final int INNER_TEST=6;

    /** Represents an empty board square. Value: @{value} */
	public static final int EMPTY=0;

	/** Represents player 1 (Black) on the game board . Value: @{value} */
	public static final int BLACK=1;

	/** Represents player 2 (White) on the game board. Value: @{value} */
	public static final int WHITE=2;
	
	/**
	 * This method initializes the game board bases on the scenario chosen.
	 * 
	 * @param mode - indicates the game mode.   
	 */
	public void initialize(int mode){
		switch (mode)
		{
		case CORNER_TEST: 
			board=new int[][]{
				{2, 0, 0, 0, 0, 0, 0, 1},
				{0, 1, 0, 0, 0, 0, 2, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 1, 0, 0, 0, 0, 1, 0},
                {2, 0, 0, 0, 0, 0, 0, 2}};

            break;
		case OUTER_TEST:
			board = new int[][] {
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 2, 2, 2, 2, 2, 2, 0},
				{0, 2, 1, 1, 1, 1, 2, 0},
				{0, 2, 1, 0, 0, 1, 2, 0},
				{0, 2, 1, 0, 0, 1, 2, 0},
				{0, 2, 1, 1, 1, 1, 2, 0},
				{0, 2, 2, 2, 2, 2, 2, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
			break;
		case INNER_TEST:
			board = new int[][] {
				{2, 2, 2, 2, 2, 2, 2, 2},
				{2, 0, 0, 0, 0, 0, 0, 2},
				{2, 0, 2, 2, 2, 2, 0, 2},
				{2, 0, 2, 1, 1, 2, 0, 2},
				{2, 0, 2, 1, 1, 2, 0, 2},
				{2, 0, 2, 2, 2, 2, 0, 2},
				{2, 0, 0, 0, 0, 0, 0, 2},
				{2, 2, 2, 2, 2, 2, 2, 2}};
			break;
		case UNWINNABLE:
			board = new int[][] {
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
			break;
		case TEST_CAPTURE:
			board=new int[][]{
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 0},
				{0, 1, 1, 1, 1, 1, 1, 0},
				{0, 1, 2, 2, 2, 1, 1, 0},
				{0, 1, 2, 0, 2, 1, 1, 0},
				{0, 1, 2, 2, 2, 1, 1, 0},
				{0, 1, 1, 1, 1, 1, 1, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
				break;
				
		case TEST_CAPTURE2:
			board=new int[][]{
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 2, 2, 2, 1, 2, 1, 1},
				{1, 2, 2, 2, 2, 2, 1, 1},
				{1, 2, 2, 0, 2, 2, 1, 1},
				{1, 2, 2, 2, 2, 1, 1, 1},
				{1, 2, 1, 2, 2, 2, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1}};
				break;
		default:
			board = new int[][]{
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 2, 1, 0, 0, 0},
				{0, 0, 0, 1, 2, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
				
		}	
	}
  
	/**
	 * This method returns the game board.
	 *
	 * @param x - Row of the board.
	 * @param y - Column of the board.
	 * @return int[][] board - The game board.  
	 */
    public int getBoard(int x, int y){
		return board[x][y];
	}

	/**
	 * This method checks the game board for a valid move.
	 *
	 * @param x - Row of the board.
	 * @param y - Column of the board.
	 * @param player - The player who's turn it is.
	 * @return boolean isValid - If there is a valid move or not.  
	 */
    public boolean isValid(int x, int y, int player){
		int opponent, temp, index;
		boolean isValid = false;
		int[][] tempArray = new int[100][100];

		flagged.clear();
		
		if (player == 1){
			opponent = 2;
		}
		else {
			opponent = 1;
		}

		//if a peice is already there return false.
		if(board[x][y] != EMPTY){
			return false;
		}

		//Check to the right
		index = 0;
		if (y <= 6){
			if (board[x][y+1] == opponent){
				for(int i = y + 1; i < 8; i++){
					if(board[x][i] == EMPTY){
						break;
					}
					else if(board[x][i] == player){
						isValid = true;
						for(int j = 0; j < index; j++){
							flagged.add(tempArray[j][0]);
							flagged.add(tempArray[j][1]);
						}
						break;
					}
					else{
						tempArray[index][0] = x;
						tempArray[index][1] = i;
						index++;
					}
				}
			}
		}	

		//Check to the left
		index = 0;
		if (y >= 2){
			if (board[x][y-1] == opponent){
				for(int i = y - 1; i > -1; i--){
					if(board[x][i] == EMPTY){
						break;
					}
					else if(board[x][i] == player){
						isValid = true;
						for(int j = 0; j < index; j++){
							flagged.add(tempArray[j][0]);
							flagged.add(tempArray[j][1]);
						}
						break;
					}
					else{
						tempArray[index][0] = x;
						tempArray[index][1] = i;
						index++;
					}
				}
			}
		}

		//Check down
		index = 0;
		if (x <= 6){
			if (board[x+1][y] == opponent){
				for(int i = x + 1; i < 8; i++){
					if(board[i][y] == EMPTY){
						break;
					}
					else if(board[i][y] == player){
						isValid = true;
						for(int j = 0; j < index; j++){
							flagged.add(tempArray[j][0]);
							flagged.add(tempArray[j][1]);
						}
						break;
					}
					else{
						tempArray[index][0] = i;
						tempArray[index][1] = y;
						index++;
					}
				}
			}
		}

		//Check up
		index = 0;
		if (x >= 2){
			if (board[x-1][y] == opponent){
				for(int i = x - 1; i > -1; i--){
					if(board[i][y] == EMPTY){
						break;
					}
					else if(board[i][y] == player){
						isValid = true;
						for(int j = 0; j < index; j++){
							flagged.add(tempArray[j][0]);
							flagged.add(tempArray[j][1]);
						}
						break;
					}
					else{
						tempArray[index][0] = i;
						tempArray[index][1] = y;
						index++;
					}
				}
			}
		}	

		//Diagonal up right
		index = 0;
		if (x >= 2 && y <= 6){
			if (board[x-1][y+1] == opponent){
				temp = y + 1;
				for(int i = x - 1; i > -1; i--){
					if (temp == 8){
						break;
					}
					if(board[i][temp] == EMPTY){
						break;
					}
					else if(board[i][temp] == player){
						isValid = true;
						for(int j = 0; j < index; j++){
							flagged.add(tempArray[j][0]);
							flagged.add(tempArray[j][1]);
						}
						break;
					}
					else{
						tempArray[index][0] = i;
						tempArray[index][1] = temp;
						index++;
					}
					temp++;
				}
			}
		}

		//Diagonal up left
		index = 0;
		if (x >= 2 && y >= 2){
			if (board[x-1][y-1] == opponent){
				temp = y - 1;
				for(int i = x - 1; i > -1; i--){
					if (temp == 8 || temp < 0){
						break;
					}
					if(board[i][temp] == EMPTY){
						break;
					}
					else if(board[i][temp] == player){
						isValid = true;
						for(int j = 0; j < index; j++){
							flagged.add(tempArray[j][0]);
							flagged.add(tempArray[j][1]);
						}
						break;
					}
					else{
						tempArray[index][0] = i;
						tempArray[index][1] = temp;
						index++;
					}
					temp--;
				}
			}
		}

		//Diagonal down right
		index = 0;
		if (x <= 6 && y <= 6){
			if (board[x+1][y+1] == opponent){
				temp = y + 1;
				for(int i = x + 1; i < 8; i++){
					if (temp == 8){
						break;
					}
					if(board[i][temp] == EMPTY){
						break;
					}
					else if(board[i][temp] == player){
						isValid = true;
						for(int j = 0; j < index; j++){
							flagged.add(tempArray[j][0]);
							flagged.add(tempArray[j][1]);
						}
						break;
					}
					else{
						tempArray[index][0] = i;
						tempArray[index][1] = temp;
						index++;
					}
					temp++;
				}
			}
		}

		//Diagonal down left
		index = 0;
		if (x <= 6 && y >= 2){
			if (board[x+1][y-1] == opponent){
				temp = y - 1;
				for(int i = x + 1; i <8; i++){
					if (temp == 8 || temp < 0){
						break;
					}
					if(board[i][temp] == EMPTY){
						break;
					}
					else if(board[i][temp] == player){
						isValid = true;
						for(int j = 0; j < index; j++){
							flagged.add(tempArray[j][0]);
							flagged.add(tempArray[j][1]);
						}
						break;
					}
					else{
						tempArray[index][0] = i;
						tempArray[index][1] = temp;
						index++;
					}
					temp--;
				}
			}
		}
		return isValid;
	}

	/**
	 * This method returns the number of pieces flipped and the player move.
	 *
	 * @param x - Row of the board.
	 * @param y - Column of the board.
	 * @param player - The player who's turn it is.
	 * @return int numFlipped - Number of pieces flipped.  
	 */
	public int move (int x, int y, int player){
		int temp, tempX, tempY, numFlipped = 0;

		if (isValid(x,y,player)){
			board[x][y] = player;
			for(int i = 0; i < flagged.size(); i++){
				tempX = flagged.get(i);
				i++;
				tempY = flagged.get(i);
				board[tempX][tempY] = player;
				numFlipped++;
			}
			return numFlipped;
		}

		return 0;
	}

	/**
	 * This method determines if the current player can move.
	 *
	 * @param player - The current player.
	 * @return boolean - True, if valid move. False, if not.  
	 */
	public boolean canMove(int player){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if (isValid(i,j,player)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method counts the amount of chips for each player.
	 *
	 * @param player - The current player.
	 * @return int count - The amount of chips.  
	 */
	public int getChips(int player){
		int count = 0;

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if (board[i][j] == player){
					count++;
				}
			}
		}

		return count;
	}
}