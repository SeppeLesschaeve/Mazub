package jumpingalien.model;

import annotate.Basic;

public class Position <T extends  Number> {
	
	private T xCoordinate;
	private T yCoordinate;

	public Position(T xCoordinate, T yCoordinate) {
		this.setXCoordinate(xCoordinate);
		this.setYCoordinate(yCoordinate);
	}
	
	@Basic
	public T getXCoordinate() {
		return xCoordinate;
	}
	
	public void setXCoordinate(T xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	
	@Basic
	public T getYCoordinate() {
		return yCoordinate;
	}
	
	public void setYCoordinate(T yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

    public Position<T> copy() {
		return new Position<>(xCoordinate,yCoordinate);
    }
}
