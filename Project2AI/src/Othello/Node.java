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
	private int immediateMobility;//how many new places can I go if I place here
	private int potentialMobility;//how many enemy chips are around this space
	//X squares (B2 B7 G2 G7)
	//C squares (A2 A7 B1 B8 G1 G8 H2 H7)

	/**
	 * Constructor for walls
	 */
	public Node(boolean color, int row, int col) {
		this.color = color;
		this.row = row;
		this.col = col;
	}

	/**
	 * calculate potential mobility of placing a piece in a particular spot
	 * @param turn - whose turn it is
	 * @param b - the board
	 * @param row - the row of the spot we are looking at
	 * @param col - the col of the spot we are looking at
	 */
	public void calculatePotentialMobility(char turn, Board b, int row, int col)
	{
		//boolean adjacent = b.getBoardSpaceValue(row, col);
		boolean color;
		int numOpposite = 0;
		this.setPotentialMobility(numOpposite);//reset potential mobility for this node
		if(turn == 'B')
		{
			color = false;
		}
		else
		{
			color = true;
		}
		int i = 0;//loop control variable
		//check all 8 adjacent nodes assuming valid move can be made there
		while(i < 8)
		{
			switch(i)
			{
				case 0:
					if(b.legalMove(row - 1, col - 1, turn, false) == true)
					{
						if(b.getBoardSpaceValue(row - 1, col - 1) != color)
						{
							numOpposite++;
						}
					}
					
				case 1:
					if(b.legalMove(row - 1, col, turn, false) == true)
					{
						if(b.getBoardSpaceValue(row - 1, col) != color)
						{
							numOpposite++;
						}
					}
				case 2:
					if(b.legalMove(row - 1, col + 1, turn, false) == true)
					{
						if(b.getBoardSpaceValue(row - 1, col + 1) != color)
						{
							numOpposite++;
						}
					}
				case 3:
					if(b.legalMove(row, col - 1, turn, false) == true)
					{
						if(b.getBoardSpaceValue(row, col - 1) != color)
						{
							numOpposite++;
						}
					}
				case 4:
					if(b.legalMove(row, col + 1, turn, false) == true)
					{
						if(b.getBoardSpaceValue(row, col) != color)
						{
							numOpposite++;
						}
					}
				case 5:
					if(b.legalMove(row + 1, col - 1, turn, false) == true)
					{
						if(b.getBoardSpaceValue(row + 1, col - 1) != color)
						{
							numOpposite++;
						}
					}
				case 6:
					if(b.legalMove(row + 1, col, turn, false) == true)
					{
						if(b.getBoardSpaceValue(row + 1, col) != color)
						{
							numOpposite++;
						}
					}
				case 7:
					if(b.legalMove(row + 1, col + 1, turn, false) == true)
					{
						if(b.getBoardSpaceValue(row + 1, col + 1) != color)
						{
							numOpposite++;
						}
					}
			}
		}
		this.setPotentialMobility(numOpposite);//number of adjacent spaces that are opposite color
		
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

	public int getImmediateMobility() {
		return immediateMobility;
	}

	public void setImmediateMobility(int immediateMobility) {
		this.immediateMobility = immediateMobility;
	}

	public int getPotentialMobility() {
		return potentialMobility;
	}

	public void setPotentialMobility(int potentialMobility) {
		this.potentialMobility = potentialMobility;
	}
	
}
