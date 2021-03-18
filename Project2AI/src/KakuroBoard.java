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
	private String boardName = "kakuroBoard15x15.txt";


	/**
	 * Constructor
	 */
	public KakuroBoard(String boardtx) throws IOException{
		boardName = boardtx;
		Cell[][] boardTxt = IO();//Grab board
		//init values
		this.board = boardTxt;
		this.rowNum = boardTxt.length;
		this.colNum = boardTxt[0].length;
		this.csp = new CSP(board);			
	}
	/**
	 * Solves Kakuro Board with 6 different Alg.
	 */
	public void solveKakuro() throws IOException {
		//vars for timing
		long start;
		long finish;
		long timeInMSecs;
		boolean passed = false;
		long numRuns = 5;//number of times board is run
		long [] avgs = {0,0,0,0,0};
		//names of Algorithms
		String [] names = {"BackTrackingSimple","BackTrackingWPartitions","BackTrackingAC3", "BackTrackingWForwardChecking","BackTrackingWForwardCheckingWPartitions"};

		//label
		System.out.println("********"+rowNum+"x"+colNum+"********");	
		
		//Add and redduce Values	
		//#1
		for(int i = 0; i < numRuns; i++)
		{
			//Reset Board and CSP init vals
			board = IO();
			csp = new CSP(board);
			csp.addNeighborsAndPosValues(csp.getBoard());
			start = System.nanoTime();//Start timer
			passed = csp.BackTracking(csp.getAllNonWallCells().getFirst());
			finish = System.nanoTime();//end timer
			timeInMSecs = (finish-start)/1000;
			avgs[0] += timeInMSecs;//add ith time to tracker
		}
		System.out.println("BackTrackingSimple: "+passed+" : Loops: "+csp.getCountRuns());

		//#2
		//Reset Board and CSP init vals
		for(int i = 0; i < numRuns; i++)
		{
			board = IO();
			csp = new CSP(board);
			start = System.nanoTime();//Start timer
			csp.addNeighborsAndPosValues(csp.getBoard());
			passed = csp.BackTrackingWPartitions(csp.getAllNonWallCells().getFirst());
			finish = System.nanoTime();//end timer
			timeInMSecs = (finish-start)/1000;
			avgs[1] += timeInMSecs;//add ith time to tracker
		}
		System.out.println("BackTrackingWPartitions: "+passed+" : Loops: "+csp.getCountRuns());

		//#3
		//Reset Board and CSP init vals
		for(int i = 0; i < numRuns; i++)
		{
			board = IO();
			csp = new CSP(board);
			start = System.nanoTime();//Start timer
			csp.addNeighborsAndPosValues(csp.getBoard());
			Set<Integer>[][] domains= csp.grabDomains(csp.getBoard());
			passed= csp.BackTrackingAC3(csp.getAllNonWallCells().getFirst(), domains);
			finish = System.nanoTime();//end timer
			timeInMSecs = (finish-start)/1000;
			avgs[2] += timeInMSecs;//add ith time to tracker
		}
		System.out.println("BackTrackingAC3: "+passed+" : Loops: "+csp.getCountRuns());

		//#4
		//Reset Board and CSP init vals
		for(int i = 0; i < numRuns; i++)
		{
			board = IO();
			csp = new CSP(board);
			csp.addNeighborsAndPosValues(csp.getBoard());
			start = System.nanoTime();//Start timer
			Set<Integer> dom = new HashSet<Integer>();
			for(int d: csp.getAllNonWallCells().getFirst().getDomain())
				dom.add(d);
			passed = csp.BackTrackingWForwardChecking(csp.getAllNonWallCells().getFirst(),dom);
			finish = System.nanoTime();//end timer
			timeInMSecs = (finish-start)/1000;
			avgs[3] += timeInMSecs;//add ith time to tracker
		}
		System.out.println("BackTrackingWForwardChecking: "+passed+" : Loops: "+csp.getCountRuns());

		//#5
		//Reset Board and CSP init vals
		for(int i = 0; i < numRuns; i++)
		{
			board = IO();
			csp = new CSP(board);
			start = System.nanoTime();//Start timer
			//Add and reduce Values		
			csp.addNeighborsAndPosValues(csp.getBoard());
			Set<Integer> dom2 = new HashSet<Integer>();
			for(int d: csp.getAllNonWallCells().getFirst().getDomain())
				dom2.add(d);
			passed = csp.BackTrackingWForwardCheckingWPartitions(csp.getAllNonWallCells().getFirst(), dom2);
			finish = System.nanoTime();//end timer
			timeInMSecs = (finish-start)/1000;
			avgs[4] += timeInMSecs;//add ith time to tracker
		}
		System.out.println("BackTrackingWForwardCheckingWPartitions: "+passed+" : Loops: "+csp.getCountRuns());
		
		//Number of loops for partitions
		System.out.println("Number of Loops for Partitions: "+csp.getCountRunsOfPartitions());

		//label
		System.out.println("***Times***");	
		//calculate and print out averages with labels
		for(int i = 0; i < 5; i++)
		{
			avgs[i] = avgs[i]/numRuns;
			System.out.println("Average for " + names[i] + ": "+ avgs[i]+" mSec");
		}
		System.out.println();
	}

	/*************************Input parsing of files **************************/

	public Cell[][] IO() throws IOException {

		//Parsing Neighbors File
		File gameFile = new File(boardName); //Grab file
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