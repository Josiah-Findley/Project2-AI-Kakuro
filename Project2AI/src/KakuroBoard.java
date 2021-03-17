import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private CSP csp;


	/**
	 * Constructor
	 */
	public KakuroBoard() throws IOException{

		Cell[][] boardTxt = IO();//Grab board

		//init values
		this.board = boardTxt;
		this.rowNum = boardTxt.length;
		this.colNum = boardTxt[0].length;
		this.csp = new CSP(board);	
		//System.out.println(csp.toString());

		addNeighborsAndPosValues();

	}



	public void solveKakuro() throws IOException {
		long start;
		long finish;
		long timeInMSecs;

		csp = new CSP(board);
		start = System.nanoTime();//Start timer
		//Add and reduce Values	



		//#1
		System.out.println("BackTrackingSimple: ");
		System.out.println(csp.BackTracking(csp.getAllNonWallCells().getFirst()));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
		System.out.println();

		//#2
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		start = System.nanoTime();//Start timer
		addNeighborsAndPosValues();//Add and reduce Values	
		System.out.println("BackTrackingWPartitions: ");
		System.out.println(csp.BackTrackingWPartitions(csp.getAllNonWallCells().getFirst()));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
		System.out.println();

		//#3
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		start = System.nanoTime();//Start timer
		addNeighborsAndPosValues();//Add and reduce Values	
		System.out.println("BackTrackingAC3: ");
		System.out.println(csp.BackTrackingAC3(csp.getAllNonWallCells().getFirst()));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
		System.out.println();

		//#4
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		start = System.nanoTime();//Start timer
		System.out.println("BackTrackingWForwardChecking: ");
		Set<Integer> dom = new HashSet<Integer>();
		for(int d: csp.getAllNonWallCells().getFirst().getDomain())
			dom.add(d);
		System.out.println(csp.BackTrackingWForwardChecking(csp.getAllNonWallCells().getFirst(),dom));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
		System.out.println();

		//#5
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		start = System.nanoTime();//Start timer
		//Add and reduce Values		
		addNeighborsAndPosValues();
		System.out.println("BackTrackingWForwardCheckingWPartitions: ");
		Set<Integer> dom2 = new HashSet<Integer>();
		for(int d: csp.getAllNonWallCells().getFirst().getDomain())
			dom2.add(d);
		System.out.println(csp.BackTrackingWForwardCheckingWPartitions(csp.getAllNonWallCells().getFirst(), dom2));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
		System.out.println();


		//#6
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);

		//add Neighbors and Possible Values
		start = System.nanoTime();//Start timer
		//Add and reduce Values
		addNeighborsAndPosValues();
		System.out.println("BackTrackingWForwardCheckingAC3: ");
		Set<Integer> dom3 = new HashSet<Integer>();
		for(int d: csp.getAllNonWallCells().getFirst().getDomain())
			dom3.add(d);
		System.out.println(csp.BackTrackingWForwardCheckingAC3(csp.getAllNonWallCells().getFirst(),dom3));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
		System.out.println();

		//#7
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);

		//add Neighbors and Possible Values
		start = System.nanoTime();//Start timer
		//Add and reduce Values
		addNeighborsAndPosValues();
		System.out.println("BackTrackingWForwardCheckingWPartitionsAC3: ");
		Set<Integer> dom4 = new HashSet<Integer>();
		for(int d: csp.getAllNonWallCells().getFirst().getDomain())
			dom4.add(d);
		System.out.println(csp.BackTrackingWForwardCheckingWPartitionsAC3(csp.getAllNonWallCells().getFirst(),dom3));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
		System.out.println();






		//New CSP to account for updated board

		//Reduce on H and V Possible values
		//csp.reduceOnPosValues();
		//Reduce Using AC3
		//csp.AC3(csp.arcs());

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
					ArrayList<Cell> horizNeighborhoodCopy = new ArrayList<Cell>(horizNeighborhood);
					horizNeighborhoodCopy.remove(n);
					n.setHorizNeighbors(horizNeighborhoodCopy);						
					//Add possible horiz Values
					n.setHorizPosVals(csp.findPartitions(leftVal, horizNeighborhood.size()));
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
					n.setVertPosVals((csp.findPartitions(vertVal, vertNeighborhood.size())));
				}
			}
		}	
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



	public CSP getCsp() {
		return csp;
	}



	public void setCsp(CSP csp) {
		this.csp = csp;
	}
}
