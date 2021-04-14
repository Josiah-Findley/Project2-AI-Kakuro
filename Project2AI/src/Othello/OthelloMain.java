package Othello;

import java.util.ArrayList;

public class OthelloMain {

	/**
	 * Main method to run a round of othello
	 * @param args command line parameters
	 */
	public static void main(String[] args) {
		// you may change the input to experiment with other boards
		Board b = new Board();
	}

	public static void gameLoop(Board gameBoard) {
		
		
	}


	/**
	 * Alpha Beta Searching
	 * @param state - current state
	 * @param turn - turn color
	 * @param depth - max depth of search 
	 */
	public static int[] alphaBetaSearch(Board state, int depth, char turn) {
		int[] action;//init action	
		if(turn=='W') { //if whites turn 		
			action = maxValue(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}	 
		else {
			action = minValue(state, depth, Integer.MAX_VALUE, Integer.MIN_VALUE);
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
	public static int[] maxValue(Board state, int depth, int alpha, int beta) {
		if(depth == 0||state.isFull())//terminal conditions
			return new int[]{heurestic(state), -1, -1};

		int v = Integer.MIN_VALUE;  //set val
		int[] action = {-1,-1}; //init action

		//if no available actions : compute but don't cahnge board
		if(state.actions('W').isEmpty()) {
			int minVal = minValue(state, depth--, alpha, beta)[0];
			v = java.lang.Math.max(v, minVal);
			if(v>=beta) //return val if greater than equal to beta
				return new int[]{v, action[0], action[1]};//return val and action		
		}
		//otherwise compute, take turn, and change board
		else {
			for(int[] a: state.actions('W')) {//for each action
				Board copy = new Board(state);
				copy.legalMove(a[0], a[1], 'B', true);
				//grab minVal
				int minVal = minValue(copy, depth--, alpha, beta)[0];
				if(v < minVal) //Store action
					action = a;
				v = java.lang.Math.max(v, minVal);
				if(v>=beta) //return val if greater than equal to beta
					return new int[]{v, action[0], action[1]};//return val and action
				//update alpha to larger of alpha and v
				alpha = java.lang.Math.max(alpha, v); 		
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
	public static int[] minValue(Board state, int depth, int alpha, int beta) {
		if(depth == 0||state.isFull())//terminal conditions
			return new int[]{heurestic(state), -1, -1};

		int v = Integer.MAX_VALUE; //set val  
		int[] action = {-1,-1}; //init action

		//if no available actions : compute but don't cahnge board
		if(state.actions('B').isEmpty()) {
			int minVal = maxValue(state, depth--, alpha, beta)[0];
			v = java.lang.Math.min(v, minVal);
			if(v<=alpha) //return val if less than equal to alpha
				return new int[]{v, action[0], action[1]};//return val and action		
		}
		//otherwise compute, take turn, and change board
		else {
			for(int[] a: state.actions('B')) {//for each action
				Board copy  = new Board(state);
				copy.legalMove(a[0], a[1],'B', true);
				//grab maxVal
				int maxVal = maxValue(copy, depth--, alpha, beta)[0];
				if(v > maxVal) //Store action
					action = a;
				v = java.lang.Math.min(v, maxVal);
				if(v<=alpha)//return val if less than equal to alpha
					return new int[]{v, action[0], action[1]}; //return val and action
				//update alpha to larger of alpha and v
				beta = java.lang.Math.min(beta, v); 		
			}	
		}
		return new int[]{v, action[0], action[1]}; //return val and action
	}


	

	/*public static Board makeMoveAI(Board prevState, int[] action, char turn) {
    	Board copyToRet = new Board(prevState);
    	copyToRet.getBoard()[action[0]][action[1]] = turn;
    	return copyToRet;
    }

    public static Board makeActualMove(Board state, int[] action, char turn) {
    	state.getBoard()[action[0]][action[1]] = turn;

    	return copyToRet;
    }*/





}
