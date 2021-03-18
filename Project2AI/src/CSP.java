import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CSP {	
	//data members
	private Cell[][] board;
	private int rowNum;
	private int colNum;
	private LinkedList<Cell> allNonWallCells;

	//Constructor
	public CSP(Cell[][] board){
		this.board = board;
		this.rowNum = board.length;
		this.colNum = board[0].length;
		allNonWallCells = addNonWalls(board);//Add all nonWalls
	}

	/**
	 * Create deepCopy of Table
	 */
	public Set<Integer>[][] deepCopy(Set<Integer>[][] board){
		Set<?>[][] deepCopy = new Set<?>[board.length][board[0].length];
		for (int i = 0; i < deepCopy.length; i++)
			for (int j = 0; j < deepCopy[0].length; j++)
				if(!this.board[i][j].getIsWall()) {
					Set<Integer> dom = new HashSet<Integer>();
					for(int d: board[i][j])
						dom.add(d);
					deepCopy[i][j] = dom;
				}
		return (Set<Integer>[][]) deepCopy;
	}

	public Set<Integer>[][] grabDomains(Cell[][] board){
		Set<?>[][] domains = new Set<?>[board.length][board[0].length];
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				if(!board[i][j].getIsWall()) {
					Set<Integer> dom = new HashSet<Integer>();
					for(int d: board[i][j].getDomain())
						dom.add(d);
					domains[i][j] = dom;
				}
		return (Set<Integer>[][]) domains;
	}

	/**
	 * add Neighbors vert and horiz for all cells 
	 * add Possible Values vert and horiz for all cells
	 */
	public void addNeighborsAndPosValues(Cell[][] board) {

		/**************Going Horizontal***************/
		int counter;
		for (int row = 0; row < rowNum; row++) {
			//reindex
			counter = 0;
			while(counter<colNum) {
				//go until not wall
				while(counter<colNum && board[row][counter].getIsWall()) {
					counter++;	
				}
				//if at end of board
				if(counter==colNum)
					break;

				//get left wall val
				int leftVal =(board[row][counter-1].getuTvalue());
				//horiz neighborhood
				ArrayList<Cell> horizNeighborhood = new ArrayList<Cell>();
				//While not wall
				while(counter<colNum && !board[row][counter].getIsWall()) {
					horizNeighborhood.add(board[row][counter]);//Add to neighborhood
					counter++;					
				}

				//Add horizontal neighbors for each member of neighborhood
				for(Cell n: horizNeighborhood) {
					ArrayList<Cell> horizNeighborhoodCopy = new ArrayList<Cell>(horizNeighborhood);
					horizNeighborhoodCopy.remove(n);
					n.setHorizNeighbors(horizNeighborhoodCopy);						
					//Add possible horiz Values
					n.setHorizPosVals(findPartitions(leftVal, horizNeighborhood.size()));
				}
			}		
		}

		/**************Going Vertical***************/
		int counterVert;
		for (int col = 0; col < rowNum; col++) {
			//reindex
			counterVert = 0;
			while(counterVert<rowNum) {
				//go until not wall
				while(counterVert<rowNum && board[counterVert][col].getIsWall()) {
					counterVert++;	
				}
				//if at end of board
				if(counterVert==rowNum)
					break;

				//get up wall val
				int vertVal =(board[counterVert-1][col].getlTvalue());
				//horiz neighborhood
				ArrayList<Cell> vertNeighborhood = new ArrayList<Cell>();

				//While not wall
				while(counterVert<colNum && !board[counterVert][col].getIsWall()) {
					vertNeighborhood.add(board[counterVert][col]);//Add to neighborhood
					counterVert++;					
				}

				//Add vertical neighbors for each member of neighborhood
				for(Cell n: vertNeighborhood) {
					ArrayList<Cell> vertNeighborhoodCopy = new ArrayList<Cell>(vertNeighborhood);
					vertNeighborhoodCopy.remove(n);
					n.setVertNeighbors(vertNeighborhoodCopy);

					//Add possible vertical Values
					n.setVertPosVals((findPartitions(vertVal, vertNeighborhood.size())));
				}
			}
		}	
	}


	/**
	 * Create list of nonWalls
	 */
	public LinkedList<Cell> addNonWalls(Cell[][] board) {
		LinkedList<Cell> allNonWallCells= new LinkedList<Cell>();
		//add all nonWalls to linked list
		for (int row = 0; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {			
				if(!board[row][col].getIsWall()){		
					allNonWallCells.add(board[row][col]);
				}
			}
		}
		allNonWallCells.add(null); //null to signal end
		return allNonWallCells;
	}

	/*************************AC3**************************/
	/**
	 * Create all Arcs 
	 */
	public Queue<Cell[]> arcs(Cell x) {
		Queue<Cell[]> arcs= new LinkedList<Cell[]>();
		//For each Cell that is not a wall add arc to queue

		//Horizontal arcs
		if(x.getHorizNeighbors()!=null){
			for(Cell c: x.getHorizNeighbors()) {
				Cell[] arc= new Cell[2];
				arc[0]=x;
				arc[1]= c;
				arcs.add(arc);	
			}
		}
		//Vertical arcs
		if(x.getVertNeighbors()!=null){
			for(Cell c: x.getVertNeighbors()) {
				Cell[] arc= new Cell[2];
				arc[0]=x;
				arc[1]= c;
				arcs.add(arc);	
			}	
		}
		return arcs;
	}
	/**
	 * Run AC3
	 */
	public boolean AC3(Queue<Cell[]> csp, Set<Integer>[][] domains){
		while (!csp.isEmpty()) {//While queue not empty
			Cell[] topOfQueue = csp.remove();
			if (Revise(topOfQueue[0],topOfQueue[1], domains[topOfQueue[0].getRow()][topOfQueue[0].getCol()],domains[topOfQueue[1].getRow()][topOfQueue[1].getCol()])) {
				if (domains[topOfQueue[0].getRow()][topOfQueue[0].getCol()].size() == 0) 
					return false;
				//push back on stack if revision
				if(topOfQueue[0].getHorizNeighbors()!=null)
					for(Cell x: topOfQueue[0].getHorizNeighbors()) {
						csp.add(new Cell[]{x,topOfQueue[0]});
					}	
				if(topOfQueue[0].getVertNeighbors()!=null)
					for(Cell x: topOfQueue[0].getVertNeighbors()) {
						csp.add(new Cell[]{x,topOfQueue[0]});
					}	
			}
		}
		return true;
	}
	/**
	 * Revise domians on two cells
	 */
	public boolean Revise(Cell x, Cell y, Set<Integer> xDomain, Set<Integer> yDomain) {
		boolean revised = false;//if revised
		//See if a vertical or horizontal pair
		//for each value in domain
		if(x.getVertNeighbors()!=null&&x.getVertNeighbors().contains(y)) {
			/*****Vertical*****/
			//Values to remove from domain
			HashSet<Integer> vTrashSet = new HashSet<Integer>();

			//For each value in x domain if there is a y value that satisfies the constraints
			for (int a: xDomain) {
				Set<Integer> vertValues = new HashSet<Integer>();//Set of values of Horiz Neighhood
				//Satified on H 
				boolean satisfiesVConstraints = false;	
				for(int yV: yDomain) {//For each val in yDom
					boolean duplicateVal = false; //Check to see if value has been used
					vertValues.clear();
					vertValues.add(a);//Add self
					if(yV==a)
						duplicateVal = true; //set true if already exist

					vertValues.add(yV);//Add self
					//Set of Partitions
					Set<Integer> targetVSet= new HashSet<Integer>();
					//For each partition
					for(int[] hp:x.getVertPosVals()) {
						targetVSet.clear();
						for(int k: hp)
							targetVSet.add(k);	//Change to set
						if(targetVSet.containsAll((vertValues))&&!duplicateVal) //if a partition contains the neighborhood
							satisfiesVConstraints = true;	
					}
				}
				//if not satisfies
				if(!satisfiesVConstraints)
					vTrashSet.add(a);//remove
			}
			//If contains int
			if(!vTrashSet.isEmpty())
				revised = true;//revised
			//Remove trashed domain values
			for(int a: vTrashSet)
				xDomain.remove(a);

		}
		/*****Horizontal*****/
		else {
			//Values to remove from domain
			HashSet<Integer> hTrashSet = new HashSet<Integer>();
			//For each value in x domain if there is a y value that satisfies the constraints
			for (int a: xDomain) {
				Set<Integer> hvalues = new HashSet<Integer>();//Set of values of Horiz Neighhood
				//Satified on H 
				boolean satisfiesHConstraints = false;	
				for(int yV: yDomain) {//For each val in yDom
					boolean duplicateVal = false; //Check to see if value has been used
					hvalues.clear();
					hvalues.add(a);//Add self
					if(yV==a)
						duplicateVal = true; //set true if already exist
					hvalues.add(yV);//Add self
					//Set of Partitions
					Set<Integer> targetHSet= new HashSet<Integer>();
					//For each partition
					for(int[] hp:x.getHorizPosVals()) {
						targetHSet.clear();
						for(int k: hp)
							targetHSet.add(k);	//Change to set
						if(targetHSet.containsAll((hvalues))&&!duplicateVal) //if a partition contains the neighborhood
							satisfiesHConstraints = true;	
					}
				}
				//if not satisfies
				if(!satisfiesHConstraints)
					hTrashSet.add(a);//remove
			}
			//If contains int
			if(!hTrashSet.isEmpty())
				revised = true;//revised
			//Remove trashed domain values
			for(int a: hTrashSet)
				xDomain.remove(a);
		}

		/****Trash Partitions****/
		//trashPartitions(x); 
		return revised;
	}
	/**
	 * Trash needless partitions 
	 */
	public void trashPartitions(Cell x) {
		/****Trash Partitions****/
		//Remove all vpartitions that don't have any values in domain
		ArrayList<int[]> trashVPVals = new ArrayList<int[]>();
		for(int[] vp: x.getVertPosVals()) {
			boolean removeVPartition = true;
			for(int i:vp) {
				if(x.getDomain().contains(i))//if one value in partition
					removeVPartition = false;
			}
			if(removeVPartition)
				trashVPVals.add(vp);
		}
		//Remove all V trashed partitions
		for(int[] trasVp:trashVPVals)
			x.getVertPosVals().remove(trasVp);

		//Remove all hpartitions that don't have any values in domain	
		ArrayList<int[]> trashHPVals = new ArrayList<int[]>();
		for(int[] hp: x.getHorizPosVals()) {
			boolean removeHPartition = true;
			for(int i:hp) {
				if(x.getDomain().contains(i))//if one value in partition
					removeHPartition = false;
			}
			if(removeHPartition)
				trashHPVals.add(hp);				
		}
		//Remove all H trashed partitions
		for(int[] trasHp:trashHPVals)
			x.getHorizPosVals().remove(trasHp);
	}
	/*************************Simple BackTracking**************************/
	public boolean BackTracking(Cell x) {
		//for each value in domain
		for(int a: x.getDomain()) {
			//If Constraints are met
			if(checkConstraints(x, a)) {
				x.setValue(a);//Set value to a
				//Grab next Cell
				Cell nextC = allNonWallCells.get(allNonWallCells.indexOf(x)+1);	
				if(nextC!=null) {//If not end of board
					//Recursion
					if(BackTracking(nextC))
						return true;//return true if solved
				}
				else {//If end of Board
					return true;//Found solved board
				}
			}
			x.setValue(0);//Reset x value during backtracking
		}	
		return false; //Else return false
	}
	public boolean BackTrackingWPartitions(Cell x) {
		//for each value in domain
		for(int a: x.getDomain()) {	
			//If Constraints are met
			if(checkConstraintsWPartitions(x, a) ) {
				x.setValue(a);//Set value to a
				//Grab next Cell
				Cell nextC = allNonWallCells.get(allNonWallCells.indexOf(x)+1);
				if(nextC!=null) {//If not end of board
					//Recursion
					if(BackTrackingWPartitions(nextC))
						return true;//return true if solved
				}
				else {//If end of Board
					return true;//Found solved board
				}
			}
			x.setValue(0);//Reset x value during backtracking
		}	
		return false; //Else return false
	}

	/*************************Forward Checking**************************/
	public boolean BackTrackingWForwardChecking(Cell x, Set<Integer> xDom) {
		//for each int in domain
		for(int a: xDom) {		
			x.getDomain().clear();
			x.getDomain().add(a);
			x.setValue(a);//Set value to a		

			//If Constraints are met
			if(ForwardChecking(x)) {
				//Grab next Cell
				Cell nextC = allNonWallCells.get(allNonWallCells.indexOf(x)+1);	
				if(nextC!=null) {//If not end of board
					//Recursion
					Set<Integer> dom = new HashSet<Integer>();
					for(int d: nextC.getDomain())
						dom.add(d);
					if(BackTrackingWForwardChecking(nextC, dom))
						return true;//return true if solved
				}
				else {//If end of Board
					return true;//Found solved board
				}
			}
			x.setValue(0);//Reset x value during backtracking
		}	
		x.setDomain(xDom);

		return false; //Else return false
	}

	public boolean BackTrackingWForwardCheckingWPartitions(Cell x, Set<Integer> xDom) {
		//for each int in domain
		for(int a: xDom) {		
			x.getDomain().clear();
			x.getDomain().add(a);
			x.setValue(a);//Set value to a		

			//If Constraints are met
			if(ForwardCheckingWPartition(x)) {
				//Grab next Cell
				Cell nextC = allNonWallCells.get(allNonWallCells.indexOf(x)+1);	
				if(nextC!=null) {//If not end of board
					//Recursion
					Set<Integer> dom = new HashSet<Integer>();
					for(int d: nextC.getDomain())
						dom.add(d);
					if(BackTrackingWForwardCheckingWPartitions(nextC, dom))
						return true;//return true if solved
				}
				else {//If end of Board
					return true;//Found solved board
				}
			}
			x.setValue(0);//Reset x value during backtracking
		}	
		x.setDomain(xDom);

		return false; //Else return false
	}

	public boolean ForwardChecking(Cell x) {
		//Check against self
		if(!checkConstraints(x, x.getValue()))
			return false;

		// Check H neighbors
		if(x.getHorizNeighbors()!=null){
			for(Cell c: x.getHorizNeighbors()) {
				boolean satisfiesH = false;
				//Deep Copy of domain
				HashSet<Integer> copyDom = new HashSet<Integer>();
				for(int n: c.getDomain())
					copyDom.add(n);
				//if val set
				if(c.getValue()!=0) {
					if(checkHConstraints(c, c.getValue())) {
						satisfiesH=true;
					}						
				}
				//If value not set
				else {
					for(int a: c.getDomain()) {	//go through domain
						if(checkHConstraints(c, a)) {//check constraints
							satisfiesH=true;
						}
						else {//remove if const not satisfied
							copyDom.remove(a);
						}
					}	
					//if dom is empty return false
					if(copyDom.size()==0) {
						return false;
					}
				}	
				//If one neighbor doesn't satisfy return false
				if(!satisfiesH)
					return false;
			}
		}
		// Check V neighbors
		if(x.getVertNeighbors()!=null){

			for(Cell c: x.getVertNeighbors()) {		
				boolean satisfiesV = false;
				//Deep Copy of domain
				HashSet<Integer> copyDom = new HashSet<Integer>();
				for(int n: c.getDomain())
					copyDom.add(n);
				//If val set
				if(c.getValue()!=0) {
					if(checkVConstraints(c, c.getValue())) {
						satisfiesV=true;
					}			
				}
				//If value not set
				else {
					for(int a: c.getDomain()) {	//go through domain
						if(checkVConstraints(c, a)) {//check constraints
							satisfiesV=true;
						}
						else {//remove if const not satisfied
							copyDom.remove(a);
						}
					}	
					//if dom is empty return false
					if(copyDom.size()==0) {
						return false;
					}
				}	
				//If one neighbor doesn't satisfy return false
				if(!satisfiesV)
					return false;
			}
		}

		return true;//Return true if no problem
	} 

	public boolean ForwardCheckingWPartition(Cell x) {
		//Check against self
		if(!checkConstraints(x, x.getValue()))
			return false;

		// Check H neighbors
		if(x.getHorizNeighbors()!=null){
			for(Cell c: x.getHorizNeighbors()) {
				boolean satisfiesH = false;
				//Deep Copy of domain
				HashSet<Integer> copyDom = new HashSet<Integer>();
				for(int n: c.getDomain())
					copyDom.add(n);
				//if val set
				if(c.getValue()!=0) {
					if(checkHConstraintsWPartitions(c, c.getValue())) {
						satisfiesH=true;
					}						
				}
				//If value not set
				else {
					for(int a: c.getDomain()) {	//go through domain
						if(checkHConstraintsWPartitions(c, a)) {//check constraints
							satisfiesH=true;
						}
						else {//remove if const not satisfied
							copyDom.remove(a);
						}
					}	
					//if dom is empty return false
					if(copyDom.size()==0) {
						return false;
					}
				}	
				//If one neighbor doesn't satisfy return false
				if(!satisfiesH)
					return false;
			}
		}
		// Check V neighbors
		if(x.getVertNeighbors()!=null){
			for(Cell c: x.getVertNeighbors()) {		
				boolean satisfiesV = false;
				//Deep Copy of domain
				HashSet<Integer> copyDom = new HashSet<Integer>();
				for(int n: c.getDomain())
					copyDom.add(n);
				//If val set
				if(c.getValue()!=0) {
					if(checkVConstraintsWPartitions(c, c.getValue())) {
						satisfiesV=true;
					}			
				}
				//If value not set
				else {
					for(int a: c.getDomain()) {	//go through domain
						if(checkVConstraintsWPartitions(c, a)) {//check constraints
							satisfiesV=true;
						}
						else {//remove if const not satisfied
							copyDom.remove(a);
						}
					}	
					//if dom is empty return false
					if(copyDom.size()==0) {
						return false;
					}
				}	
				//If one neighbor doesn't satisfy return false
				if(!satisfiesV)
					return false;
			}

		}
		return true;//Return true if no problem
	} 

	/*************************AC3**************************/
	public boolean BackTrackingAC3(Cell x, Set<Integer>[][] dcB) {
		Set<Integer> xDom = dcB[x.getRow()][x.getCol()];//grab domain of x
		Set<Integer> dom = new HashSet<Integer>();//make deep copy
		for(int d: xDom)
			dom.add(d);
		//for each value in domain
		for(int a: dom) {
			xDom.clear();
			xDom.add(a);//set xDom
			x.setValue(a);//Set value to a	
			Set<Integer>[][] dcBoard = deepCopy(dcB);	
			//If Constraints are met
			if(AC3(arcs(x), dcBoard)) {
				//Grab next Cell
				Cell nextC = allNonWallCells.get(allNonWallCells.indexOf(x)+1);
				if(nextC!=null) {//If not end of board
					//Recursion
					if(BackTrackingAC3(nextC, dcBoard))
						return true;//return true if solved
				}
				else {//If end of Board
					return true;//Found solved board
				}
			}
			//reset values
			dcBoard[x.getRow()][x.getCol()] = dcB[x.getRow()][x.getCol()];
			x.setValue(0);
		}	
		return false; //Else return false
	}

	public boolean BackTrackingWForwardCheckingAC3(Cell x, Set<Integer>[][] dcB) {
		Set<Integer> xDom = dcB[x.getRow()][x.getCol()];//grab domain of x
		Set<Integer> dom = new HashSet<Integer>();//make deep copy
		for(int d: xDom)
			dom.add(d);
		//for each value in domain
		for(int a: dom) {
			xDom.clear();
			xDom.add(a);//set xDom
			x.setValue(a);//Set value to a	
			Set<Integer>[][] dcBoard = deepCopy(dcB);//make deep copy
			//If Constraints are met
			if(AC3(arcs(x), dcBoard)) {
				//Grab next Cell
				Cell nextC = allNonWallCells.get(allNonWallCells.indexOf(x)+1);
				if(nextC!=null) {//If not end of board
					//Recursion
					if(BackTrackingWForwardCheckingAC3(nextC, dcBoard))
						return true;//return true if solved
				}
				else {//If end of Board
					return true;//Found solved board
				}
			}
			//reset values
			dcBoard[x.getRow()][x.getCol()] = dcB[x.getRow()][x.getCol()];
			x.setValue(0);
		}	
		return false; //Else return false
	}

	/*************************Checking Constraints**************************/

	/**
	 * find distinct partitions of a particular integer at a certain length
	 * @param x - the cell we want to check constraints of
	 * @param a - the value for x that we are checking constraints for
	 */
	public boolean checkHConstraintsWPartitions(Cell x, int a) {
		boolean duplicateVal = false; //Check to see if value has been used
		//Satified on H and V
		boolean satisfiesHConstraints = false;	

		//******Horizontal******//
		Set<Integer> hvalues = new HashSet<Integer>();//Set of values of Horiz Neighhood
		hvalues.add(a);//Add self
		for(Cell y: x.getHorizNeighbors()) {//Add neighbors
			if(y.getValue()!=0)
				hvalues.add(y.getValue());
			if(y.getValue()==a)
				duplicateVal = true; //set true if already exist
		}
		//Set of Partitions
		Set<Integer> targetHSet= new HashSet<Integer>();
		//For each partition
		for(int[] hp:x.getHorizPosVals()) {
			targetHSet.clear();
			for(int k: hp)
				targetHSet.add(k);	//Change to set
			if(targetHSet.containsAll((hvalues))) //if a partition contains the neighborhood
				satisfiesHConstraints = true;	
		}
		return satisfiesHConstraints&&!duplicateVal;

	}
	/**
	 * find distinct partitions of a particular integer at a certain length
	 * @param x - the cell we want to check constraints of
	 * @param a - the value for x that we are checking constraints for
	 */
	public boolean checkConstraintsWPartitions(Cell x, int a) {
		return checkHConstraintsWPartitions(x,a)&&checkVConstraintsWPartitions(x,a);

	}
	/**
	 * find distinct partitions of a particular integer at a certain length
	 * @param x - the cell we want to check constraints of
	 * @param a - the value for x that we are checking constraints for
	 */
	public boolean checkVConstraintsWPartitions(Cell x, int a) {	
		boolean duplicateVal = false; //Check to see if value has been used
		//Satified on H and V	
		boolean satisfiesVConstraints = false;

		//******Vertical******//
		Set<Integer> vertValues = new HashSet<Integer>();//Set of values of Horiz Neighhood
		vertValues.add(a);//Add self
		for(Cell y: x.getVertNeighbors()) {//Add neighbors
			if(y.getValue()!=0)
				vertValues.add(y.getValue());
			if(y.getValue()==a)
				duplicateVal = true;//set true if already exist
		}				
		//Set of Partitions
		Set<Integer> targetVSet= new HashSet<Integer>();
		//For each partition
		for(int[] vp:x.getVertPosVals()) {
			targetVSet.clear();
			for(int k: vp)
				targetVSet.add(k);	//Change to set
			if(targetVSet.containsAll(vertValues)) //if a partition contains the neighborhood
				satisfiesVConstraints = true;	
		}
		return satisfiesVConstraints&&!duplicateVal;
	}
	/**
	 * find distinct partitions of a particular integer at a certain length
	 * @param x - the cell we want to check constraints of
	 * @param a - the value for x that we are checking constraints for
	 */
	public boolean checkHConstraints(Cell x, int a) {
		boolean duplicateVal = false; //Check to see if value has been used
		//Satified on H 
		boolean satisfiesHConstraints = false;	

		//******Horizontal******//
		Set<Integer> hvalues = new HashSet<Integer>();//Set of values of Horiz Neighhood
		hvalues.add(a);//Add self
		if(x.getHorizNeighbors()!=null) { 
			for(Cell y: x.getHorizNeighbors()) {//Add neighbors
				hvalues.add(y.getValue());
				if(y.getValue()==a)
					duplicateVal = true; //set true if already exist
			}
			//Generate sums
			int hSum =0;
			for(int k: x.getHorizPosVals().get(0))
				hSum+=k;	//add to sum
			int hValSum =0;
			for(int k: hvalues)
				hValSum+=k;	//add to sum
			//If a value has a 0, then sum can be up to hSum
			if(hvalues.contains(0)) {
				if(hValSum<=hSum) //if a partition contains the neighborhood
					satisfiesHConstraints = true;
			}
			//else sum must equal hsum
			else {
				if(hValSum==hSum) //if a partition contains the neighborhood
					satisfiesHConstraints = true;
			}
		}
		else {
			int hValSum =0;
			for(int k: hvalues)
				hValSum+=k;	//add to sum
			if(hValSum==a) //if a partition contains the neighborhood
				satisfiesHConstraints = true;
		}


		return satisfiesHConstraints&&!duplicateVal;

	}
	/**
	 * find distinct partitions of a particular integer at a certain length
	 * @param x - the cell we want to check constraints of
	 * @param a - the value for x that we are checking constraints for
	 */
	public boolean checkVConstraints(Cell x, int a) {
		boolean duplicateVal = false; //Check to see if value has been used
		//Satified on V
		boolean satisfiesVConstraints = false;
		//******Vertical******/
		Set<Integer> vertValues = new HashSet<Integer>();//Set of values of Horiz Neighhood
		vertValues.add(a);//Add self
		if(x.getVertNeighbors()!=null) {
			for(Cell y: x.getVertNeighbors()) {//Add neighbors
				vertValues.add(y.getValue());
				if(y.getValue()==a)
					duplicateVal = true;//set true if already exist
			}			
			//Generate sums
			int vSum =0;
			for(int k: x.getVertPosVals().get(0))
				vSum+=k;	//add to sum
			int vValSum =0;
			for(int k: vertValues)
				vValSum+=k;	//add to sum
			//If a value has a 0, then sum can be up to vSum
			if(vertValues.contains(0)) {
				if(vValSum<=vSum) //if a partition contains the neighborhood
					satisfiesVConstraints = true;
			}	
			//else sum must equal vSum
			else {
				if(vValSum==vSum) //if a partition contains the neighborhood
					satisfiesVConstraints = true;
			}	
		}
		else {
			int vValSum =0;
			for(int k: vertValues)
				vValSum+=k;	//add to sum
			if(vValSum==a) //if a partition contains the neighborhood
				satisfiesVConstraints = true;
		}

		return satisfiesVConstraints&&!duplicateVal;
	}
	/**
	 * find distinct partitions of a particular integer at a certain length
	 * @param x - the cell we want to check constraints of
	 * @param a - the value for x that we are checking constraints for
	 */
	public boolean checkConstraints(Cell x, int a) {
		return checkHConstraints( x, a)&&checkVConstraints(x, a);		
	}

	/*************************Finding Partitions**************************/

	/**
	 * find distinct partitions of a particular integer at a certain length
	 * @param value - the number we want partitions of
	 * @param length - length of a partition that we want
	 */
	public ArrayList<int[]> findPartitions(int value, int length) {
		int[] array = new int [value];//hold partition, length of number we want but will never be that long

		//Store in ArrayList of int arrays
		ArrayList<int[]> posValues= new ArrayList<int[]>();

		//use every int 1 to n as starting point
		for(int i = 1; i < value; i++)
		{
			array[0] = i;//initilize starting point        
			partitions(value, 0, array, 0, length,posValues);        
		}
		return posValues;
	}

	/**
	 * helper function to find distinct partitions of a particular integer at a certain length
	 * @param target - the number we want partions of
	 * @param curr - sum of the numbers we already have
	 * @param array - array of numbers we are using
	 * @param idx - track index of array we are at
	 * @param desiredLength - length of a partition that we want
	 */
	static void partitions(int target, int curr, int[] array, int idx, int desiredLength, ArrayList<int[]> posValues)
	{
		//store array in string to find length
		String temp = "";
		for (int i=0; i <= idx; i++)
		{
			temp += array[i];
		}
		//if we have created a partion of the target and have desired length
		if (curr + array[idx] == target && temp.length() == desiredLength)
		{
			int[] posVal = new int[desiredLength];
			for (int i=0; i <= idx; i++)
			{
				posVal[i]= array[i];
			}
			posValues.add(posVal);
			return;
		}
		//if we have numbers that go past our target 
		else if (curr + array[idx] > target)
		{
			return;
		}
		else
		{
			//recursive, add next value to curr, add 1 to index, for each index of array
			for(int i = array[idx]+1; i < 10; i++)
			{
				array[idx+1] = i;
				partitions(target, curr + array[idx], array, idx+1, desiredLength, posValues);
			}
		}
	}

	/*************************toString**************************/
	public String toString(Cell[][] board) {
		String returned ="";
		for (int row = 0; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {
				//Horizontal arcs

				if(!board[row][col].getIsWall()){
					returned += board[row][col].toString()+"\n";
				}
			}
		}
		return returned;	
	}

	/*************************Getters and Setters**************************/

	public Cell[][] getBoard() {
		return board;
	}

	public void setBoard(Cell[][] board) {
		this.board = board;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public LinkedList<Cell> getAllNonWallCells() {
		return allNonWallCells;
	}

	public void setAllNonWallCells(LinkedList<Cell> allNonWallCells) {
		this.allNonWallCells = allNonWallCells;
	}

}
