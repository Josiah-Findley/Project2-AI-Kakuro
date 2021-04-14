package Othello;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Board {
	private char board[][];
	private final char boardSize = 8;

	public Board () {
		board = new char[boardSize][boardSize];	
		init();
		printBoard(1);
	}
	
	
	/**
	 * Create deepCopy of Board
	 */
	
	public Board (Board orig) {
		board = new char[orig.board.length][orig.board[0].length];
		for (int i = 0; i < orig.board.length; i++)
			for (int j = 0; j < orig.board[0].length; j++)		
					board[i][j] = orig.board[i][j];				
	}

	public void init() {
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = 'O'; //empty cells
			}

		//Initial start
		board[3][3] = 'B';
		board[4][4] = 'B';
		board[3][4] = 'W';
		board[4][3] = 'W';
	}


		/**
	     * This method prints the board to the console
	     * @param turn current turn
	     */
	   public void printBoard(int turn) {
	        int numBlacks = 0;
	        int numWhites = 0;
	        char[] topLabel = {'A','B','C','D','E','F','G','H'};
	        System.out.println();
	        System.out.printf("   ");
	        for (int i = 0; i < boardSize; i++) {
	            System.out.printf(" " + topLabel[i] + "  ");
	        }
	        System.out.printf("\n  ");
	        for (int i = 0; i < boardSize; i++) {
	            System.out.printf("----");
	        }
	        System.out.println();
	        for (int i = 0; i < boardSize; i++) {
	            System.out.printf((i+1) + " |");
	            for (int j = 0; j < boardSize; j++) {
	                if (board[i][j] == 'W') {
	                    System.out.printf(" W |");
	                    numWhites++;
	                } else if (board[i][j] == 'B') {
	                    System.out.printf(" B |");
	                    numBlacks++;
	                } 
	                /*else if (isValid(i, j, turn)) {
	                    System.out.printf(" * |");}*/
	                 else {
	                    System.out.printf("   |");
	                }
	            }
	            System.out.println();
	            System.out.printf("  ");
	            for (int j = 0; j < boardSize; j++) {
	                System.out.printf("----");
	            }
	            System.out.println();
	 
	        }
	        System.out.println("Black: " + numBlacks + " - " + "White: " + numWhites);
	        System.out.println(); 
	    }
	   
	   /**
	     * Board is full
	     */
	    public boolean isFull(){
	    	//For each space check valid moves
	    	for (int i = 0; i < board.length; i++)
				for (int j = 0; j < board.length; j++)		
					if(board[i][j]=='O') {//if moves possible
						return false;
						}
	    	return true;	//return true if board is full
	    }
	   
	   
	    /**
	     * Actions available
	     * @param turn - turn color
	     */
	    public ArrayList<int[]> actions(char turn){
	    	ArrayList<int[]> actions = new ArrayList<int[]>();
	   	
	    	//For each space check valid moves
	    	for (int i = 0; i < board.length; i++)
				for (int j = 0; j < board.length; j++)		
					if(legalMove(i, j, turn, false)) {//if valid move
						int[] action = {i,j};
						actions.add(action); // add to possible actions
						}
	    	return actions;	//return poss actions
	    }
	   
	   
	   /**
	     *  Decide if the move is legal
	     *  Modified from https://github.com/haly/Othello/blob/master/Game.java
	     *
	     *  @param    row        row in the board matrix
	     *  @param    col        column in the board matrix
	     *  @param    color      color of the player - Black or White
	     *  @param    flip       true if the player wants to flip the discs
	     *
	     *  @return              true if the move is legal, else false
	     */
	    public boolean legalMove(int row, int col, char color, boolean flip) 
		{
			// Initialize boolean legal as false
			boolean legal = false;
			
			// If the cell is empty, begin the search
			// If the cell is not empty there is no need to check anything 
			// so the algorithm returns boolean legal as is
			if (board[row][col] == 0)
			{
				// Initialize variables
				int posX;
				int posY;
				boolean found;
				int current;
				
				// Searches in each direction
				// x and y describe a given direction in 9 directions
				// 0, 0 is redundant and will break in the first check
				for (int x = -1; x <= 1; x++)
				{
					for (int y = -1; y <= 1; y++)
					{
						// Variables to keep track of where the algorithm is and
						// whether it has found a valid move
						posX = col + x;
						posY = row + y;
						found = false;
						current = board[posY][posX];
						
						// Check the first cell in the direction specified by x and y
						// If the cell is empty, out of bounds or contains the same color
						// skip the rest of the algorithm to begin checking another direction
						if (current == -1 || current == 0 || current == color)
						{
							continue;
						}
						
						// Otherwise, check along that direction
						while (!found)
						{
							posX += x;
							posY += y;
							current = board[posY][posX];
							
							// If the algorithm finds another piece of the same color along a direction
							// end the loop to check a new direction, and set legal to true
							if (current == color)
							{
								found = true;
								legal = true;
								
								// If flip is true, reverse the directions and start flipping until 
								// the algorithm reaches the original location
								if (flip)
								{
									posX -= x;
									posY -= y;
									current = board[posY][posX];
									
									while(current != 0)
									{
										board[posY][posX] = color;
										posX -= x;
										posY -= y;
										current = board[posY][posX];
									}
								}
							}
							// If the algorithm reaches an out of bounds area or an empty space
							// end the loop to check a new direction, but do not set legal to true yet
							else if (current == -1 || current == 0)
							{
								found = true;
							}
						}
					}
				}
			}
	        return legal;
	    }
	    
		/*************************Getters and Setters**************************/
	    
		public char[][] getBoard() {
			return board;
		}


		public void setBoard(char[][] board) {
			this.board = board;
		}


		public char getBoardSize() {
			return boardSize;
		}

	}



