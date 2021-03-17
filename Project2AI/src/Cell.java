import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Cell {


	//Data Members
	private Boolean isWall;
	private int value;
	private int uTvalue;
	private int lTvalue;
	private int row;
	private int col;
	private Set<Integer> domain;
	private ArrayList<int[]> horizPosVals;
	private ArrayList<int[]> vertPosVals;
	private ArrayList<Cell> horizNeighbors;
	private ArrayList<Cell> vertNeighbors;
	
	
	public Cell(int lTvalue, int uTvalue, int row, int col) {
		this.isWall = true;
		this.row = row;
		this.col = col;
		this.uTvalue = uTvalue;
		this.lTvalue = lTvalue;
	}
	
	//for nodes that are added by
	public Cell(int value, int row, int col) {
		this.isWall = false;
		this.value = value;
		this.row = row;
		this.col = col;
		this.domain = new HashSet<Integer>();
		for(int i=1;i<10;i++)
			domain.add(i);	
		
	}
	
	public Cell(Cell c) {
        this.isWall = c.isWall;
        this.value = c.value;
		this.row = c.row;
		this.col = c.col;
		this.domain = new HashSet<Integer>();
		for(int i:c.getDomain())
			domain.add(i);	
    }
	public String toString() {
		String returned = "";
		returned+="("+row+","+col+"): "+domain.toString()+" : "+value;

		return returned;
	}
	
	
	
	/*************************Getters and Setters**************************/
	public Boolean getIsWall() {
		return isWall;
	}
	public void setIsWall(Boolean isWall) {
		this.isWall = isWall;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getuTvalue() {
		return uTvalue;
	}
	public void setuTvalue(int uTvalue) {
		this.uTvalue = uTvalue;
	}
	public int getlTvalue() {
		return lTvalue;
	}
	public void setlTvalue(int lTvalue) {
		this.lTvalue = lTvalue;
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

	public Set<Integer> getDomain() {
		return domain;
	}

	public void setDomain(Set<Integer> domain) {
		this.domain = domain;
	}

	public ArrayList<int[]> getHorizPosVals() {
		return horizPosVals;
	}

	public void setHorizPosVals(ArrayList<int[]> horizPosVals) {
		this.horizPosVals = horizPosVals;
	}

	public ArrayList<int[]> getVertPosVals() {
		return vertPosVals;
	}

	public void setVertPosVals(ArrayList<int[]> vertPosVals) {
		this.vertPosVals = vertPosVals;
	}

	public ArrayList<Cell> getHorizNeighbors() {
		return horizNeighbors;
	}

	public void setHorizNeighbors(ArrayList<Cell> horizNeighbors) {
		this.horizNeighbors = horizNeighbors;
	}

	public ArrayList<Cell> getVertNeighbors() {
		return vertNeighbors;
	}

	public void setVertNeighbors(ArrayList<Cell> vertNeighbors) {
		this.vertNeighbors = vertNeighbors;
	}



}
