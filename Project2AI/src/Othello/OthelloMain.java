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
    
    public static void alphaBetaSearch(Board state, int depth, char turn) {
    	int v = maxValue(state, 0, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    	
    }
    
    public static int maxValue(Board state, int curDepth, int depth, int alpha, int beta) {
    	if(curDepth == depth )
    		System.out.println();
    		
    	int v = Integer.MIN_VALUE;
    	
    	for(int[] a: actions(state)) {
    		v = java.lang.Math.max(v, minValue(makeMoveAI(state, a), curDepth+1, depth, alpha, beta));
    		if(v>=beta)
    			return v;
    		alpha = java.lang.Math.max(alpha, v); 		
    	}
    	
    	
    }

    public static int minValue(Board state, int curDepth, int depth, int alpha, int beta) {
	
    	int v = Integer.MAX_VALUE;
    	
    	for(int[] a: actions(state)) {
    		v = java.lang.Math.min(v, maxValue(makeMoveAI(state, a), curDepth+1, depth, alpha, beta));
    		if(v<=alpha)
    			return v;
    		beta = java.lang.Math.min(beta, v); 		
    	}
	
    }
    
    public static ArrayList<int[]> actions(Board state){
    	ArrayList<int[]> actions = new ArrayList<int[]>();
    	actions
    	
    	
    	return null;	
    }
    
    public static Board makeMoveAI(Board prevState, int[] action, char turn) {
    	Board copyToRet = new Board(prevState);
    	copyToRet.getBoard()[action[0]][action[1]] = turn;
    	return copyToRet;
    }
    
    public static Board makeActualMove(Board state, int[] action, char turn) {
    	state.getBoard()[action[0]][action[1]] = turn;
    	
    	return copyToRet;
    }
	
}
