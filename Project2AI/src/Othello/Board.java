package Othello;

public class Board {
	private char board[][];
	private final char boardSize = 8;

	public Board () {
		board = new char[boardSize][boardSize];	
		init();
		printBoard(1);
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
	            System.out.printf(i + " |");
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
	
	   
	   

	}



