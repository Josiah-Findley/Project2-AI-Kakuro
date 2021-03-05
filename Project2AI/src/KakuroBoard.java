import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javafx.application.Application;


public class KakuroBoard {
	//data members
	private Cell[][] board;
	private int rowNum;
	private int colNum;
	
	
	public KakuroBoard() throws IOException{
		Cell[][] boardTxt = IO();//Grab board
		//init values
		this.board = boardTxt;
		this.rowNum = boardTxt.length;
		this.colNum = boardTxt[0].length;
	}
    
    
    
	/*************************Input parsing of files **************************/
	
	public static Cell[][] IO() throws IOException {

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
						board[rowCount][colCount] = new Cell(Integer.valueOf(cell));					
					}
					//otherwise
					else {
					String[] wall = cell.split(",");//Split line
					board[rowCount][colCount] = new Cell(Integer.valueOf(wall[0]), Integer.valueOf(wall[1]));	
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
