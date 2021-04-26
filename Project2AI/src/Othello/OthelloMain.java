package Othello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class OthelloMain {

	/**
	 * Main method to run a round of othello
	 * @param args command line parameters
	 */
	public static void main(String[] args) {
		// you may change the input to experiment with other boards
		Board b = new Board();
		gameLoop(b);
		System.out.println("\n\n**Normal Termination**");
	}
	
	/*
	 * Runs game and takes in initial gameBoard
	 */
	public static void gameLoop(Board gameBoard) {

		//Data variables
		boolean whiteComp = false;
		boolean blackComp = false;
		
		char white = 'W';
		char black = 'B';
		
		int whiteDepth = 8;
		int blackDepth = 8;
	
		//Print initial board
		gameBoard.printBoard(white);

		//while game not over
		while(!gameBoard.isFull() && (gameBoard.actions(black).size()!=0 || gameBoard.actions(white).size()!=0)) {			
			//whites turn
			if(gameBoard.actions(white).size()!=0) {//if available actions
				if(!whiteComp)//if still humans move
					whiteComp = makeHumanMove(white, gameBoard);
				if(whiteComp) {//if comps move
					int[] compMove = alphaBetaSearch(gameBoard, whiteDepth, white,"getHeuristicDiscsMovesCorners");
					//if move legal
					if(compMove[0]!=-1)
						gameBoard.legalMove(compMove[0], compMove[1], white, true);						
				}
					
				gameBoard.printBoard(black);//print board
			}
			//blacks turn
			if(gameBoard.actions(black).size()!=0) {
				if(!blackComp)//if humans move
					blackComp = makeHumanMove(black, gameBoard);
				if(blackComp) {//if comps move
					int[] compMove = alphaBetaSearch(gameBoard, blackDepth, black,"getHeuristicDiscsMovesCorners");
					//if move legal
					if(compMove[0]!=-1)
						gameBoard.legalMove(compMove[0], compMove[1], black, true);						
				}
					
				gameBoard.printBoard(white);//print board
			}
		}
	}

	/*
	 * Makes a human move
	 * @param turn - players color
	 * @param board - the current board
	 */
	public static boolean makeHumanMove(char turn, Board board) {

		System.out.println("\nPlayer "+ turn+  " enter a valid move: ");
		boolean control = true;

		while(control) {
			Scanner sc = new Scanner (System.in);//open scanner
			if(sc.hasNextLine()){
				String input = sc.nextLine();//grab input
				if(input.equalsIgnoreCase("computer")) {
					sc.close();
					return true;				
				}
					
				try {
					int colAsci = (int)Character.toUpperCase(input.charAt(0));//first input
					int rowAsci = (int)input.charAt(1);//second input

					//total incorrect move
					if(input.length()!=2) {
						System.out.println("\nEnter a valid move (e.g. A3 or B7): ");
					}
					//in correct range
					else if(colAsci>=65 && colAsci <=72 && (rowAsci)>= 48 && (rowAsci<= 56)) {
						//if legal move make move
						if(board.legalMove(rowAsci -48, colAsci-64, turn, true)) {
							control = false;
							break;
						}
						else {//try again
							System.out.println("\nEnter a valid move (e.g. A3 or B7). Valid squares are indicated by and *: ");				
						}	
					}
					else {//try again for valid input
						System.out.println("\nEnter a valid move (e.g. A3 or B7). Valid squares are indicated by and *: ");				
					}
				}catch(Exception e) {
					System.out.println("\nEnter a valid move (e.g. A3 or B7). Valid squares are indicated by and *: ");						
				}
			}
		}
		return false;
	}

	/**
	 * Alpha Beta Searching
	 * @param state - current state
	 * @param turn - turn color
	 * @param depth - max depth of search 
	 */
	public static int[] alphaBetaSearch(Board state, int depth, char turn, String heur) {
		int[] action = null;//init action	
		if(turn=='W') { //if whites turn 		
			action = maxValue(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, heur);
		}	 
		else if(turn=='B') {
			action = minValue(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, heur);
		}	
		return new int[] {action[1], action[2]}; //return last action
	}

	/**
	 * Max Value for state
	 * @param state - current state
	 * @param depth - max depth of search 
	 * @param alpha - alpha value for alpha beta pruning
	 * @param beta - beta value for alpha beta pruning
	 */
	public static int[] maxValue(Board state, int depth, int alpha, int beta, String heur) {
		if(depth == 0||state.isFull())//terminal conditions
			switch(heur) {
			case "getHeuristic":
				return new int[]{state.getHeuristic('W'), -1, -1};
			case "getHeuristic2":
				return new int[]{state.getHeuristic2('W'), -1, -1};
			case "getHeuristicDiscsMovesCorners":
				return new int[]{state.getHeuristicDiscsMovesCorners('W'), -1, -1};		
			}


		int v = Integer.MIN_VALUE;  //set val
		int[] action = {-1,-1}; //init action

		//if no available actions : compute but don't cahnge board
		if(state.actions('W').isEmpty()) {
			int minVal = minValue(state, depth-1, alpha, beta, heur)[0];
			v = java.lang.Math.max(v, minVal);
			if(v>=beta) //return val if greater than equal to beta
				return new int[]{v, action[0], action[1]};//return val and action
			alpha = java.lang.Math.max(alpha, v); 
		}
		//otherwise compute, take turn, and change board
		else {
			for(int[] a: state.actions('W')) {//for each action
				Board copy = new Board(state);
				copy.legalMove(a[0], a[1], 'W', true);
				//copy.printBoard('W');
				//System.out.println("depth: "+depth);
				//grab minVal
				int minVal = minValue(copy, depth-1, alpha, beta, heur)[0];
				//System.out.println("minVal:" +minVal);
				if(v < minVal) //Store action
					action = a;
				v = java.lang.Math.max(v, minVal);
				if(v>=beta) { //return val if greater than equal to beta
					//System.out.println("v: "+ v);
					return new int[]{v, action[0], action[1]};//return val and action
				}
				//update alpha to larger of alpha and v
				alpha = java.lang.Math.max(alpha, v); 	
				//System.out.println("alpha: "+alpha);
			}
		}
		return new int[]{v, action[0], action[1]};//return val and action
	}

	/**
	 * Min Value for state
	 * @param state - current state
	 * @param curDepth - current depth of search
	 * @param depth - max depth of search 
	 * @param alpha - alpha value for alpha beta pruning
	 * @param beta - beta value for alpha beta pruning
	 */
	public static int[] minValue(Board state, int depth, int alpha, int beta, String heur) {
		if(depth == 0||state.isFull()) {//terminal conditions
			switch(heur) {
			case "getHeuristic":
				return new int[]{state.getHeuristic('B'), -1, -1};
			case "getHeuristic2":
				return new int[]{state.getHeuristic2('B'), -1, -1};
			case "getHeuristicDiscsMovesCorners":
				return new int[]{state.getHeuristicDiscsMovesCorners('B'), -1, -1};		
			}
		}

		int v = Integer.MAX_VALUE; //set val  
		int[] action = {-1,-1}; //init action

		//if no available actions : compute but don't change board
		if(state.actions('B').isEmpty()) {
			int maxVal = maxValue(state, depth-1, alpha, beta, heur)[0];
			v = java.lang.Math.min(v, maxVal);
			if(v<=alpha) //return val if less than equal to alpha
				return new int[]{v, action[0], action[1]};//return val and action		
			beta = java.lang.Math.min(beta, v); 
		}
		//otherwise compute, take turn, and change board
		else {
			for(int[] a: state.actions('B')) {//for each action
				Board copy  = new Board(state);
				copy.legalMove(a[0], a[1],'B', true);
				//copy.printBoard('B');
				//System.out.println("depth: "+depth);
				//grab maxVal
				int maxVal = maxValue(copy, depth-1, alpha, beta, heur)[0];
				//System.out.println("maxVal: "+maxVal);
				if(v > maxVal) //Store action
					action = a;
				v = java.lang.Math.min(v, maxVal);
				if(v<=alpha) {//return val if less than equal to alpha
					//System.out.println("v: "+ v);
					return new int[]{v, action[0], action[1]}; //return val and action
				}
				//update alpha to larger of alpha and v
				beta = java.lang.Math.min(beta, v); 		
			}	
		}
		return new int[]{v, action[0], action[1]}; //return val and action
	}
}
