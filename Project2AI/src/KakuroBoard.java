import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import javafx.application.Application;


public class KakuroBoard {
	//data members
	private Cell[][] board;
	private int rowNum;
	private int colNum;

	/**
	 * Constructor
	 */
	public KakuroBoard() throws IOException{
		Cell[][] boardTxt = IO();//Grab board
		//init values
		this.board = boardTxt;
		this.rowNum = boardTxt.length;
		this.colNum = boardTxt[0].length;
	}
	
	
	
	public void solveKakuro() {
		addNeighborsAndPosValues();
		reduceOnPosValues();
		arcs();
		System.out.println(this.toString());
	}

	/**
	 * Create all Arcs 
	 */
	public Queue<Cell[]> arcs() {
		Queue<Cell[]> arcs= new LinkedList<Cell[]>();
		//For each Cell that is not a wall add arc to queue
		for (int row = 0; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {
				//Horizontal arcs
				if(board[row][col].getHorizNeighbors()!=null){
					for(Cell c: board[row][col].getHorizNeighbors()) {
						Cell[] arc= new Cell[2];
						arc[0]=board[row][col];
						arc[1]= c;
						arcs.add(arc);	
					}
				}
				//Vertical arcs
				if(board[row][col].getVertNeighbors()!=null){
					for(Cell c: board[row][col].getVertNeighbors()) {
						Cell[] arc= new Cell[2];
						arc[0]=board[row][col];
						arc[1]= c;
						arcs.add(arc);	
					}	
				}
			}
		}
		return arcs;
	}

	/**
	 * Reduce domain based on Possible H and V values
	 */
	public void reduceOnPosValues() {
		for (int row = 0; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {
				//Horizontal arcs
				if(!board[row][col].getIsWall()){
					// values to keep in domain
					HashSet<Integer> vSet = new HashSet<Integer>();
					HashSet<Integer> hSet = new HashSet<Integer>();
					//Add all vp values
					for(int[] vp: board[row][col].getVertPosVals()) {
						for(int i:vp) {
							if(!vSet.contains(i))//only if not already in 
								vSet.add(i);
						}
					}
					//Add all hp values
					for(int[] hp: board[row][col].getHorizPosVals()) {
						for(int i:hp) {
							if(!hSet.contains(i))//only if not already in 
								hSet.add(i);
						}
					}
					//Find intersection of the two
					Set<Integer> intersection = new HashSet<Integer>(vSet); // use the copy constructor
					intersection.retainAll(hSet);			
					//Update domain
					for(int i: intersection) {
						board[row][col].setDomain(intersection);					
					}
					//Remove all vpartitions that don't have any values in domain
					ArrayList<int[]> trashVPVals = new ArrayList<int[]>();
					for(int[] vp: board[row][col].getVertPosVals()) {
						boolean removeVPartition = true;
						for(int i:vp) {
							if(intersection.contains(i))//if one value in partition
								removeVPartition = false;
						}
						if(removeVPartition)
							trashVPVals.add(vp);
					}
					//Remove all V trashed partitions
					for(int[] trasVp:trashVPVals)
						board[row][col].getVertPosVals().remove(trasVp);
					
					//Remove all hpartitions that don't have any values in domain	
					ArrayList<int[]> trashHPVals = new ArrayList<int[]>();
					for(int[] hp: board[row][col].getHorizPosVals()) {
						boolean removeHPartition = true;
						for(int i:hp) {
							if(intersection.contains(i))//if one value in partition
								removeHPartition = false;
						}
						if(removeHPartition)
							trashHPVals.add(hp);				
					}
					//Remove all H trashed partitions
					for(int[] trasHp:trashHPVals)
						board[row][col].getHorizPosVals().remove(trasHp);
				}
			}
		}
	}

	/*Revise(csp, topOfQueue[0], topOfQueue[1])*/
	public boolean AC3(Queue<Cell[]> csp){
		while (!csp.isEmpty()) {
			Cell[] topOfQueue = csp.remove();
			if (true) {
				if (topOfQueue[0].getDomain().size() == 0) 
					return false;
				for(Cell x: topOfQueue[0].getHorizNeighbors()) {
					csp.add(new Cell[]{x,topOfQueue[0]});
				}	
				for(Cell x: topOfQueue[0].getVertNeighbors()) {
					csp.add(new Cell[]{x,topOfQueue[0]});
				}	
			}
		}
		return true;
	}


	/*public boolean Revise(Queue<Cell[]> csp, Cell i, Cell j) {
		boolean revised = false;
		for (int a: i.getDomain()) {
			if (no b in Dj lets (a, b) satisfy the constraints between Xi and Xj) {
				i.getDomain().remove(a)	;	
				revised = true;
			}
			return revised;
		}


		/**
		 * add Neighbors vert and horiz for all cells 
		 * add Possible Values vert and horiz for all cells
		 */
		public void addNeighborsAndPosValues() {

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
						ArrayList<Cell> horizNeighborhoodCopy = new ArrayList<Cell>();
						Collections.copy(horizNeighborhood, horizNeighborhoodCopy);
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
								ArrayList<Cell> vertNeighborhoodCopy = new ArrayList<Cell>();
								Collections.copy(vertNeighborhood, vertNeighborhoodCopy);
								vertNeighborhoodCopy.remove(n);
								n.setHorizNeighbors(vertNeighborhoodCopy);

								//Add possible vertical Values
								n.setVertPosVals((findPartitions(vertVal, vertNeighborhood.size())));
							}
						}
					}
				
			
		}

		/**
		 * find distinct partitions of a particular integer at a certain length
		 * @param value - the number we want partitions of
		 * @param length - length of a partition that we want
		 */
		public ArrayList<int[]> findPartitions(int value, int length) {
			int n = value;//number we are trying to make
			int desiredLength = length;//length we want partition to be
			int[] array = new int [n];//hold partition, length of number we want but will never be that long

			//Store in ArrayList of int arrays
			ArrayList<int[]> posValues= new ArrayList<int[]>();

			//use every int 1 to n as starting point
			for(int i = 1; i < n; i++)
			{
				array[0] = i;//initilize starting point        
				partitions(n, 0, array, 0, desiredLength,posValues);        
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
		
		
		public String toString() {
			String returned ="";
			for (int row = 0; row < rowNum; row++) {
				for (int col = 0; col < colNum; col++) {
					//Horizontal arcs
					
					if(!board[row][col].getIsWall()){
						List<Integer> sortedList = new ArrayList<>(board[row][col].getDomain());
						Collections.sort(sortedList);
						returned += ("Row: "+row+" Col: "+col+" D: "+ sortedList.toString()+"\n");
					}
				}
			}
			return returned;	
		}


		/*************************Input parsing of files **************************/

		public Cell[][] IO() throws IOException {

			//Parsing Neighbors File
			File gameFile = new File("kakuroBoard.txt"); //Grab file
			Scanner in = new Scanner(gameFile);//Scan file

			String row = in.nextLine(); //Grab line
			Scanner rowScanner = new Scanner(row);//Scan line

			int r = rowScanner.nextInt(); //num rows
			int c = rowScanner.nextInt(); //num cols

			Cell[][] board = new Cell[r][c];//init board

			//row and col counters
			int rowCount = 0;
			int colCount = 0;

			while(in.hasNextLine()) { //Goes line by line
				colCount=0;//reset colCount
				row = in.nextLine(); //Grab line
				rowScanner = new Scanner(row);//Scan line

				while(rowScanner.hasNext()) { //Go through line
					String cell = rowScanner.next(); //Grab value
					//If a enter able value
					if(cell.length()==1) {
						board[rowCount][colCount] = new Cell(Integer.valueOf(cell), rowCount, colCount);					
					}
					//otherwise
					else {
						String[] wall = cell.split(",");//Split line
						board[rowCount][colCount] = new Cell(Integer.valueOf(wall[0]), Integer.valueOf(wall[1]), rowCount, colCount);	
					}
					colCount++;//inc Col Count
				}
				rowCount++;	//inc row count
				rowScanner.close();
			}

			in.close();

			return board;
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

	}
