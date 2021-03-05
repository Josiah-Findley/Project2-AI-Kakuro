

public class Cell {


	//Data Members
	private Boolean isWall;
	private int value;
	private int uTvalue;
	private int lTvalue;
	
	public Cell(int lTvalue, int uTvalue) {
		this.isWall = true;
		this.value = 0;
		this.uTvalue = uTvalue;
		this.lTvalue = lTvalue;
	}
	
	//for nodes that are added by
	public Cell(int value) {
		this.isWall = false;
		this.value = value;
		this.uTvalue = 0;
		this.lTvalue = 0;
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



}
