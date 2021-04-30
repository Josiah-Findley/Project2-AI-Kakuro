package Othello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class OthelloMain {

	//Data Variables
	static char white = 'W';
	static char black = 'B';
	static int whiteDepth = 5;
	static int blackDepth = 17;
	static int time = 9;//seconds for search
	static int minDepth = 3;
	static boolean whiteComp = true;
	static boolean blackComp = true;
	static boolean runEndgame = true;
	static boolean iterativeDeepening = false;
	static String Heur1 = "getHeuristic";
	static String Heur2 = "getHeuristicDiscsMovesCorners";
	static String Heur3 = "getHeuristicMobility";
	static String endGameHeur = "getHeuristicCoinParityEndGame";

	/**
	 * Main method to run a round of othello
	 * @param args command line parameters
	 */
	public static void main(String[] args) {
		// you may change the input to experiment with other boards
		gameLoop();
		System.out.println("\n\n**Normal Termination**");
	}

	/*
	 * Runs game 
	 */
	public static void gameLoop() {
		Board gameBoard = new Board();//Create Board
		Scanner sc = new Scanner (System.in);//open scanner

		//Print initial board
		gameBoard.printBoard(white);
		
		//Run simulations
		for(int i=7; i<8;i++) {
			gameBoard = new Board();//Create Board
			whiteDepth = i;
			blackDepth = i;
			while(!gameBoard.isFull() && (gameBoard.actions(black,false).size()!=0 || gameBoard.actions(white,false).size()!=0)) {			
				runRound(gameBoard, Heur1, Heur1, iterativeDeepening, sc);
			}
			System.out.println(whiteDepth+" "+blackDepth);
			//gameBoard.printBoard(white);//print board
		}
		sc.close();//close scanner
	}

	/**
	 * Runs one turn of black and white
	 * @param gameBoard - current gameboard
	 * @param whiteHeur - white heuristic
	 * @param blackHeur - black heuristic
	 * @param sc - current scanner
	 */
	public static void runRound(Board gameBoard, String whiteHeur, String blackHeur, boolean iterativeDeepening, Scanner sc) {	
		//********************whites turn********************//
		if(gameBoard.actions(white, false).size()!=0) {//if available actions
			if(!whiteComp)//if still humans move
				whiteComp = makeHumanMove(white, gameBoard, sc);
			if(whiteComp) {//if comps move
				int[] compMove;
				if(gameBoard.isEndGame(whiteDepth)&&runEndgame) {//if endgame
					if(!iterativeDeepening)//no iterative deepening
						compMove = alphaBetaSearch(gameBoard, whiteDepth, white, endGameHeur);			
					else//if iterative deepening
						compMove = iterativeDeepening(gameBoard, time, minDepth, whiteDepth, white, endGameHeur);			
				}
				else{//not endgame
					if(!iterativeDeepening)//no iterative deepening
						compMove = alphaBetaSearch(gameBoard, whiteDepth, white, whiteHeur);
					else//if iterative deepening
						compMove = iterativeDeepening(gameBoard, time, minDepth, whiteDepth, white, whiteHeur);
				}
				//if move legal
				if(compMove[0]!=-1)
					gameBoard.legalMove(compMove[0], compMove[1], white, true);						
			}			
			gameBoard.printBoard(black);//print board
		}
		//********************blacks turn********************//
		if(gameBoard.actions(black, false).size()!=0) {
			if(!blackComp)//if humans move
				blackComp = makeHumanMove(black, gameBoard, sc);
			if(blackComp) {//if comps move
				int[] compMove;//comps move
				if(gameBoard.isEndGame(blackDepth)&&runEndgame) {//if endgame
					if(!iterativeDeepening)//no iterative deepening
						compMove = alphaBetaSearch(gameBoard, blackDepth, black, endGameHeur);			
					else//if iterative deepening
						compMove = iterativeDeepening(gameBoard, time, minDepth, blackDepth, black, endGameHeur);			
				}
				else{//not endgame
					if(!iterativeDeepening)//no iterative deepening
						compMove = alphaBetaSearch(gameBoard, blackDepth, black, blackHeur);
					else//if iterative deepening
						compMove = iterativeDeepening(gameBoard, time, minDepth, blackDepth, black, blackHeur);
				}
				//if move legal
				if(compMove[0]!=-1)
					gameBoard.legalMove(compMove[0], compMove[1], black, true);						
			}			
			gameBoard.printBoard(white);//print board	
		}
	}

	/*
	 * Makes a human move
	 * @param turn - players color
	 * @param board - the current board
	 */
	public static boolean makeHumanMove(char turn, Board board, Scanner sc) {
		System.out.println("\nPlayer "+ turn+  " enter a valid move: ");
		boolean control = true;//while control variable
		while(control) {
			if(sc.hasNextLine()){//if next line
				String input = sc.nextLine();//grab input
				if(input.equalsIgnoreCase("computer")) {//flip to computer
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
	 * Run iterative deepening until time is hit
	 * @param state - current board
	 * @param time - time in secs till stop
	 * @param initDepth - start depth
	 * @param depth - end depth
	 * @param turn - current turn
	 * @param heur - heuristic used
	 * @return - action returned
	 */
	public static int[] iterativeDeepening(Board state, int time, int initDepth, int depth, char turn, String heur) {
		int[] action = null;//init action
		//vars for timing
		long start;
		start = System.nanoTime();//Start timer
		for(int i =initDepth; i<=depth;i++) {//run iterative deepening until time is hit

			if((System.nanoTime()-start)/1_000_000_000<time) {//If under time
				//System.out.println((System.nanoTime()-start)/1_000_000_000);
				action = alphaBetaSearch(state, i, turn, heur);
			}
			else {//return action
				//System.out.println((System.nanoTime()-start)/1_000_000_000);
				return action;
			}
		}
		return action;
	}

	/**
	 * Alpha Beta Searching
	 * @param state - current state
	 * @param turn - turn color
	 * @param depth - max depth of search 
	 */
	public static int[] alphaBetaSearch(Board state, int depth, char turn, String heur) {
		int[] action = null;//init action	
		if(turn==white) { //if whites turn 		
			action = maxValue(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, heur);
		}	 
		else if(turn==black) {
			action = minValue(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, heur);
		}	
		System.out.println(action[0]+": HeurValue  "+" Row: "+action[1]+" Col: "+action[2]);
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
			switch(heur) {//which heuristic is being used
			case "getHeuristic":
				return new int[]{state.getHeuristic(white), -1, -1};
			case "getHeuristicMobility":
				return new int[]{state.getHeuristicMobility(white), -1, -1};
			case "getHeuristicDiscsMovesCorners":
				return new int[]{state.getHeuristicDiscsMovesCorners(white), -1, -1};	
			case "getHeuristicCoinParityEndGame":
				return new int[]{state.getHeuristicCoinParityEndGame(white), -1, -1};
			}


		int v = Integer.MIN_VALUE;  //set val
		int[] action = {-1,-1}; //init action

		//if no available actions : compute but don't cahnge board
		if(state.actions(white, false).isEmpty()) {
			int minVal = minValue(state, depth-1, alpha, beta, heur)[0];
			v = java.lang.Math.max(v, minVal);
			if(v>=beta) //return val if greater than equal to beta
				return new int[]{v, action[0], action[1]};//return val and action
			alpha = java.lang.Math.max(alpha, v); 
		}
		//otherwise compute, take turn, and change board
		else {
			for(int[] a: state.actions(white, true)) {//for each action
				Board copy = new Board(state);
				copy.legalMove(a[0], a[1], white, true);
				//copy.printBoard(white);
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
			switch(heur) {//which heuristic is being used
			case "getHeuristic":
				return new int[]{state.getHeuristic(black), -1, -1};
			case "getHeuristicMobility":
				return new int[]{state.getHeuristicMobility(black), -1, -1};
			case "getHeuristicDiscsMovesCorners":
				return new int[]{state.getHeuristicDiscsMovesCorners(black), -1, -1};		
			case "getHeuristicCoinParityEndGame":
				return new int[]{state.getHeuristicCoinParityEndGame(black), -1, -1};
			}
		}

		int v = Integer.MAX_VALUE; //set val  
		int[] action = {-1,-1}; //init action

		//if no available actions : compute but don't change board
		if(state.actions(black, false).isEmpty()) {
			int maxVal = maxValue(state, depth-1, alpha, beta, heur)[0];
			v = java.lang.Math.min(v, maxVal);
			if(v<=alpha) //return val if less than equal to alpha
				return new int[]{v, action[0], action[1]};//return val and action		
			beta = java.lang.Math.min(beta, v); 
		}
		//otherwise compute, take turn, and change board
		else {
			for(int[] a: state.actions(black, true)) {//for each action
				Board copy  = new Board(state);
				copy.legalMove(a[0], a[1],black, true);
				//copy.printBoard(black);
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
