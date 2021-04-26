package Othello;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Board {
	private char board[][];
	private final char boardSize = 10;

	/**
	 * Constructor of Board
	 */
	public Board() {
		board = new char[boardSize][boardSize];
		init();
	}

	/**
	 * Copy Constructor Create deepCopy of Board
	 */
	public Board(Board orig) {
		board = new char[orig.board.length][orig.board[0].length];
		for (int i = 0; i < orig.board.length; i++)
			for (int j = 0; j < orig.board[0].length; j++)
				board[i][j] = orig.board[i][j];
	}

	/**
	 * This method initializes the board
	 */

	public void init() {
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = 'O'; // empty cells
			}

		// init edges
		for (int i = 0; i < boardSize; i++) {
			board[i][0] = 'E';
			board[i][9] = 'E';
		}
		for (int j = 0; j < boardSize; j++) {
			board[0][j] = 'E';
			board[9][j] = 'E';
		}

		// Initial start
		board[4][4] = 'B';
		board[5][5] = 'B';
		board[4][5] = 'W';
		board[5][4] = 'W';
	}

	/**
	 * This method prints the board to the console
	 * Modified from studyPool Othello stub setup
	 * @param turn current turn
	 */
	public void printBoard(char turn) {
		int numBlacks = 0;
		int numWhites = 0;
		char[] topLabel = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
		System.out.println();
		System.out.printf("   ");
		for (int i = 0; i < boardSize - 2; i++) {
			System.out.printf(" " + topLabel[i] + "  ");
		}
		System.out.printf("\n  ");
		for (int i = 0; i < boardSize - 2; i++) {
			System.out.printf("----");
		}
		System.out.println();
		for (int i = 1; i < boardSize - 1; i++) {
			System.out.printf((i) + " |");
			for (int j = 1; j < boardSize - 1; j++) {
				if (board[i][j] == 'W') {
					System.out.printf(" W |");
					numWhites++;
				} else if (board[i][j] == 'B') {
					System.out.printf(" B |");
					numBlacks++;
				} else if (legalMove(i, j, turn, false)) {
					System.out.printf(" * |");
				} else {
					System.out.printf("   |");
				}
			}
			System.out.println();
			System.out.printf("  ");
			for (int j = 0; j < boardSize - 2; j++) {
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
	public boolean isFull() {
		// For each space check valid moves
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board.length; j++)
				if (board[i][j] == 'O') {// if moves possible
					return false;
				}
		return true; // return true if board is full
	}

	/**
	 * Actions available
	 * @param turn - turn color
	 * @param sort - sort by piece type
	 * Returns array list of actions available
	 */
	public ArrayList<int[]> actions(char turn, boolean sort) {
		ArrayList<int[]> actions = new ArrayList<int[]>();

		// For each space check valid moves
		for (int i = 1; i < board.length - 1; i++)
			for (int j = 1; j < board.length - 1; j++)
				if (legalMove(i, j, turn, false)) {// if valid move
					int[] action = { i, j };
					actions.add(action); // add to possible actions
				}

		if(sort)
			//compare and sort actions - corners then edges then others based on potential mobility
			Collections.sort(actions, new Comparator<int[]>() {
				public int compare(int[] a, int[] b) {
					if (((a[0] == 1 || a[0] == 8) && (a[1] == 1 || a[1] == 8))&&!((b[0] == 1 || b[0] == 8) && (b[1] == 1 || b[1] == 8))) {		
						return -1;
					} else if (((a[0] == 1 || a[1] == 1) || (a[0] == 8 || a[1] == 8))&&!((b[0] == 1 || b[1] == 1) || (b[0] == 8 || b[1] == 8))) {
						return -1;
					} else if (!((a[0] == 1 || a[0] == 8) && (a[1] == 1 || a[1] == 8))&&((b[0] == 1 || b[0] == 8) && (b[1] == 1 || b[1] == 8))) {		
						return 1;
					} else if (!((a[0] == 1 || a[1] == 1) || (a[0] == 8 || a[1] == 8))&&((b[0] == 1 || b[1] == 1) || (b[0] == 8 || b[1] == 8))) {
						return 1;
					} else {
						return 0;
					}		
				}
			});
		// return poss actions
		return actions;	
	}

	/**
	 * Decide if the move is legal Modified from
	 * https://github.com/haly/Othello/blob/master/Game.java
	 *
	 * @param row   row in the board matrix
	 * @param col   column in the board matrix
	 * @param color color of the player - Black or White
	 * @param flip  true if the player wants to flip the discs
	 *
	 * @return true if the move is legal, else false
	 */
	public boolean legalMove(int row, int col, char color, boolean flip) {
		// Initialize boolean legal as false
		boolean legal = false;

		// If the cell is empty, begin the search
		// If the cell is not empty there is no need to check anything
		// so the algorithm returns boolean legal as is
		if (board[row][col] == 'O') {
			// Initialize variables
			int posX;
			int posY;
			boolean found;
			char current;

			// Searches in each direction
			// x and y describe a given direction in 9 directions
			// 0, 0 is redundant and will break in the first check
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					// Variables to keep track of where the algorithm is and
					// whether it has found a valid move
					posX = col + x;
					posY = row + y;
					found = false;
					current = board[posY][posX];

					// Check the first cell in the direction specified by x and y
					// If the cell is empty, out of bounds or contains the same color
					// skip the rest of the algorithm to begin checking another direction
					if (current == 'E' || current == 'O' || current == '*' || current == color) {
						continue;
					}

					// Otherwise, check along that direction
					while (!found) {
						posX += x;
						posY += y;
						current = board[posY][posX];

						// If the algorithm finds another piece of the same color along a direction
						// end the loop to check a new direction, and set legal to true
						if (current == color) {
							found = true;
							legal = true;

							// If flip is true, reverse the directions and start flipping until
							// the algorithm reaches the original location
							if (flip) {
								posX -= x;
								posY -= y;
								current = board[posY][posX];

								while (current != 'O') {
									board[posY][posX] = color;
									posX -= x;
									posY -= y;
									current = board[posY][posX];
								}
							}
						}
						// If the algorithm reaches an out of bounds area or an empty space
						// end the loop to check a new direction, but do not set legal to true yet
						else if (current == 'E' || current == 'O' || current == '*') {
							found = true;
						}
					}
				}
			}
		}
		if (legal && flip)
			board[row][col] = color;
		return legal;
	}

	/*
	 * All heuristics were researched and then implemented from a variety of different ideas
	 */

	public int getHeuristic(char turn) {
		// give each square a certain value
		// corners are most valuable

		int[][] values = { { 10, 2, 7, 7, 7, 7, 2, 10 }, { 2, -4, 1, 1, 1, 1, -4, 2 }, { 7, 1, 1, 1, 1, 1, 1, 7 },
				{ 7, 1, 1, 1, 1, 1, 1, 7 }, { 7, 1, 1, 1, 1, 1, 1, 7 }, { 7, 1, 1, 1, 1, 1, 1, 7 },
				{ 2, -4, 1, 1, 1, 1, -4, 2 }, { 10, 2, 7, 7, 7, 7, 2, 10 } };

		int heur = 0;

		//Find the heur by finding the net sum
		for (int y = 1; y < 9; y++)
			for (int x = 1; x < 9; x++) {
				if (board[x][y] == turn)
					heur += values[y - 1][x - 1];
				else if (board[x][y] != 'O')
					heur -= values[y - 1][x - 1];
			}

		return turn == 'W' ? heur : -heur;
	}

	public int getHeuristic2(char turn) {
		// give each square a certain value
		// corners are most valuable

		int[][] values = { { 4, -3, 2, 2, 2, 2, -3, 4 }, { -3, -4, -1, -1, -1, -1, -4, -3 },
				{ 2, -1, 0, 1, 1, 0, -1, 2 }, { 2, -1, 0, 1, 1, 0, -1, 2 }, { 2, -1, 1, 0, 0, 1, -1, 2 },
				{ -3, -4, -1, -1, -1, -1, -4, -3 }, { 4, -3, 2, 2, 2, 2, -3, 4 } };

		int heur = 0;
		//Find the heur by finding the net sum
		for (int y = 1; y < 9; y++)
			for (int x = 1; x < 9; x++) {
				if (board[x][y] == turn)
					heur += values[y - 1][x - 1];
				else if (board[x][y] != 'O')
					heur -= values[y - 1][x - 1];
			}
		return turn == 'W' ? heur : -heur;
	}

	public int getHeuristicDiscsMovesCorners(char turn) {
		// give each square a certain value
		// corners are most valuable

		int[][] values = { { 100, -3, 2, 2, 2, 2, -3, 100 }, { -3, -4, -1, -1, -1, -1, -4, -3 }, { 2, -1, 1, 0, 0, 1, -1, 2 },
				{ 2, -1, 0, 1, 1, 0, -1, 2 }, { 2, -1, 0, 1, 1, 0, -1, 2 }, { 2, -1, 1, 0, 0, 1, -1, 2 },
				{ -3, -4, -1, -1, -1, -1, -4, -3 }, { 100, -3, 2, 2, 2, 2, -3, 100 } };

		int heur = 0;

		for (int y = 1; y < 9; y++)
			for (int x = 1; x < 9; x++) {
				if (board[x][y] == turn)
					heur += values[y - 1][x - 1];
				else if (board[x][y] != 'O')
					heur -= values[y - 1][x - 1];
			}

		return turn == 'W' ? heur + actions('W', false).size() : -heur-actions('B', false).size();
	}

	/**
	 * calculate potential mobility of placing a piece in a particular spot
	 * 
	 * @param turn - whose turn it is
	 * @param b    - the board
	 * @param row  - the row of the spot we are looking at
	 * @param col  - the col of the spot we are looking at
	 */
	public int calculatePotentialMobility(char turn, int row, int col) {
		// boolean adjacent = b.getBoardSpaceValue(row, col);
		boolean color;
		int numOpposite = 0;
		if (turn == 'B') {
			color = false;
		} else {
			color = true;
		}
		int i = 0;// loop control variable
		// check all 8 adjacent nodes assuming valid move can be made there
		while (i < 8) {
			switch (i) {
			case 0:
				if (legalMove(row - 1, col - 1, turn, false) == true) {
					if (getBoardSpaceValue(row - 1, col - 1) != color) {
						numOpposite++;
					}
				}
				break;

			case 1:
				if (legalMove(row - 1, col, turn, false) == true) {
					if (getBoardSpaceValue(row - 1, col) != color) {
						numOpposite++;
					}
				}
				break;
			case 2:
				if (legalMove(row - 1, col + 1, turn, false) == true) {
					if (getBoardSpaceValue(row - 1, col + 1) != color) {
						numOpposite++;
					}
				}
				break;
			case 3:
				if (legalMove(row, col - 1, turn, false) == true) {
					if (getBoardSpaceValue(row, col - 1) != color) {
						numOpposite++;
					}
				}
				break;
			case 4:
				if (legalMove(row, col + 1, turn, false) == true) {
					if (getBoardSpaceValue(row, col) != color) {
						numOpposite++;
					}
				}
				break;
			case 5:
				if (legalMove(row + 1, col - 1, turn, false) == true) {
					if (getBoardSpaceValue(row + 1, col - 1) != color) {
						numOpposite++;
					}
				}
				break;
			case 6:
				if (legalMove(row + 1, col, turn, false) == true) {
					if (getBoardSpaceValue(row + 1, col) != color) {
						numOpposite++;
					}
				}
				break;
			case 7:
				if (legalMove(row + 1, col + 1, turn, false) == true) {
					if (getBoardSpaceValue(row + 1, col + 1) != color) {
						numOpposite++;
					}
				}
				break;
			}
			i++;
		}
		return numOpposite;

	}

	/************************* Getters and Setters **************************/

	public char[][] getBoard() {
		return board;
	}

	public boolean getBoardSpaceValue(int row, int col) {
		if (board[row][col] == 'B') {
			return false;
		} else {
			return true;
		}
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

	public char getBoardSize() {
		return boardSize;
	}

}
