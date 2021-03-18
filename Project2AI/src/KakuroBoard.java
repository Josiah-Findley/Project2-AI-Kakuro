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
	private String boardName = "kakuroBoard4.txt";

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
	}
	
	/**
	 * Constructor
	 */
	public KakuroBoard(String boardtx) throws IOException{
		Cell[][] boardTxt = IO();//Grab board
		//init values
		this.board = boardTxt;
		this.rowNum = boardTxt.length;
		this.colNum = boardTxt[0].length;
		this.csp = new CSP(board);	
		boardName = boardtx;
	}
	/**
	 * Solves Kakuro Board with 6 different Alg.
	 */
	public void solveKakuro() throws IOException {
		long start;
		long finish;
		long timeInMSecs;

		//Add and reduce Values	
		//#1
		csp = new CSP(board);
		csp.addNeighborsAndPosValues(csp.getBoard());
		start = System.nanoTime();//Start timer
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
		csp.addNeighborsAndPosValues(csp.getBoard());
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
		csp.addNeighborsAndPosValues(csp.getBoard());
		System.out.println("BackTrackingAC3: ");
		Set<Integer>[][] domains= csp.grabDomains(csp.getBoard());
		System.out.println(csp.BackTrackingAC3(csp.getAllNonWallCells().getFirst(), domains));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
		System.out.println();

		//#4
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		csp.addNeighborsAndPosValues(csp.getBoard());
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
		csp.addNeighborsAndPosValues(csp.getBoard());
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
		start = System.nanoTime();//Start timer
		csp.addNeighborsAndPosValues(csp.getBoard());
		System.out.println("BackTrackingWForwardCheckingAC3: ");
		Set<Integer>[][] domains2= csp.grabDomains(csp.getBoard());
		System.out.println(csp.BackTrackingWForwardCheckingAC3(csp.getAllNonWallCells().getFirst(), domains2));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs);
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