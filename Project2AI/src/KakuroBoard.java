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
	private String boardName = "kakuroBoard3.txt";


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
		csp.addNeighborsAndPosValues(csp.getBoard());

	}



	public void solveKakuro() throws IOException {
		long start;
		long finish;
		long timeInMSecs;

		csp = new CSP(board);
		start = System.nanoTime();//Start timer
<<<<<<< HEAD
		//Add and reduce Values	



		//#1
		System.out.println("BackTrackingSimple: ");
=======
		//Add and reduce Values		
		System.out.println("BackTrackingSimple: solving " + boardName);
>>>>>>> branch 'master' of https://github.com/Josiah-Findley/Project2-AI-Kakuro.git
		System.out.println(csp.BackTracking(csp.getAllNonWallCells().getFirst()));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
<<<<<<< HEAD
		System.out.println (timeInMSecs);
		System.out.println();

		//#2
=======
		System.out.println (timeInMSecs + " mSecs");
		
>>>>>>> branch 'master' of https://github.com/Josiah-Findley/Project2-AI-Kakuro.git
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		start = System.nanoTime();//Start timer
<<<<<<< HEAD
		csp.addNeighborsAndPosValues(csp.getBoard());
		System.out.println("BackTrackingWPartitions: ");
=======
		//Add and reduce Values
		addNeighborsAndPosValues();
		System.out.println("BackTrackingWPartitions: solving " + boardName);
>>>>>>> branch 'master' of https://github.com/Josiah-Findley/Project2-AI-Kakuro.git
		System.out.println(csp.BackTrackingWPartitions(csp.getAllNonWallCells().getFirst()));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
<<<<<<< HEAD
		System.out.println (timeInMSecs);
		System.out.println();
=======
		System.out.println (timeInMSecs + " mSecs");
>>>>>>> branch 'master' of https://github.com/Josiah-Findley/Project2-AI-Kakuro.git

		//#3
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		start = System.nanoTime();//Start timer
<<<<<<< HEAD
		csp.addNeighborsAndPosValues(csp.getBoard());
		System.out.println("BackTrackingAC3: ");
		System.out.println(csp.BackTrackingAC3(csp.getAllNonWallCells().getFirst(), csp.getBoard()));
=======
		//Add and reduce Values		
		addNeighborsAndPosValues();
		System.out.println("BackTrackingWForwardChecking: solving " + boardName);
		System.out.println(csp.BackTrackingWForwardChecking(csp.getAllNonWallCells().getFirst(), csp.getAllNonWallCells().getFirst().getDomain()));
>>>>>>> branch 'master' of https://github.com/Josiah-Findley/Project2-AI-Kakuro.git
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
<<<<<<< HEAD
		System.out.println (timeInMSecs);
		System.out.println();
=======
		System.out.println (timeInMSecs + " mSecs");
		
		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		
		start = System.nanoTime();//Start timer
		//Add and reduce Values		
		addNeighborsAndPosValues();
		csp.reduceOnPosValues();
		System.out.println("BackTrackingWPartitions and init Reduction: solving " + boardName);
		System.out.println(csp.BackTrackingWPartitions(csp.getAllNonWallCells().getFirst()));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs + " mSecs");

		//Reset Board and CSP init vals
		board = IO();
		csp = new CSP(board);
		
		//add Neighbors and Possible Values
		start = System.nanoTime();//Start timer
		//Add and reduce Values
		addNeighborsAndPosValues();
		csp.reduceOnPosValues();
		//csp.AC3(csp.arcs());
		System.out.println("BackTracking and init Reduction: solving " + boardName);
		System.out.println(csp.BackTracking(csp.getAllNonWallCells().getFirst()));
		finish = System.nanoTime();//end timer
		timeInMSecs = (finish-start)/1000;
		System.out.println (timeInMSecs + " mSecs");
>>>>>>> branch 'master' of https://github.com/Josiah-Findley/Project2-AI-Kakuro.git



	

		//New CSP to account for updated board

		//Reduce on H and V Possible values
		//csp.reduceOnPosValues();
		//Reduce Using AC3
		//csp.AC3(csp.arcs());

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
