package jumpingalien.model;

import annotate.Basic;

public class Position <T extends  Number> implements Cloneable{
	
	private T x;
	private T y;
	
	public Position(T[] position) {
		this(position[0], position[1]);
	}
	
	public Position(T x, T y) {
		setPosition(x,y);
	}
	
	@Basic
	public T getX() {
		return x;
	}
	
	public void setX(T x) {
		this.x = x;
	}
	
	@Basic
	public T getY() {
		return y;
	}
	
	public void setY(T y) {
		this.y = y;
	}
	
	public void setPosition(T x, T y) {
		this.setX(x);
		this.setY(y);
	}
	
	public Position<T> clone(){
		return new Position<T>(x,y);
	}

}
