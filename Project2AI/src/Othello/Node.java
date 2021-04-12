package Othello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Node {
	//Data Members
	private boolean color; //black is false, white is true
	private int row;
	private int col;

	/**
	 * Constructor for walls
	 */
	public Node(boolean color, int row, int col) {
		this.color = color;
		this.row = row;
		this.col = col;
	}

	


	/**
	 * To String for Cell
	 */
	public String toString() {
		String returned = "";
		returned+="("+row+","+col+"): "+color;
		return returned;
	}


	/*************************Getters and Setters**************************/

	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
}
