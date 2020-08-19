package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public class Rectangle implements Cloneable{
	
	private Position<Integer> origin;
	private int width;
	private int height;
	
	public Rectangle(int x, int y, int width, int height) {
		this(new Position<>(x,y), width, height);
	}
	
	public Rectangle(Position<Integer> origin, int width, int height) {
		this.origin = origin;
		this.setDimension(width, height);
	}
	
	public Position<Integer> getOrigin(){
		return this.origin;
	}
	
	public int getXCoordinate() {
		return this.origin.getX();
	}
	
	public int getYCoordinate() {
		return this.origin.getY();
	}
	
	public void updateHorizontalComponent(int x) {
		this.origin.setX(x);
	}
	
	public void updateVerticalComponent(int y) {
		this.origin.setY(y);
	}
	
	public void setOrigin(int x, int y) {
		this.origin.setX(x);
		this.origin.setY(y);
	}
	
	public int getWidth() {
		return Integer.valueOf(width);
	}

	public void setWidth(int width) {
		if(width <= 0) throw new IllegalArgumentException("The width must be strictly positive");
		this.width = width;
	}

	public int getHeight() {
		return Integer.valueOf(height);
	}

	public void setHeight(int height) {
		if(height <= 0) throw new IllegalArgumentException("The height must be strictly positive");
		this.height = height;
	}
	
	public void setDimension(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	@Basic
	public boolean contains(Position<Integer> point) {
		return (origin.getX() <= point.getX() && point.getX() <= getWidth() - 1 && 
				origin.getY() <= point.getY() && point.getY() <= getHeight() - 1);
	}
	
	@Basic
	public boolean overlaps(Rectangle other) {
		return(!(this.origin.getY() + getHeight() - 1 < other.origin.getY() 
				|| this.origin.getY() > other.origin.getY() + other.getHeight() - 1 
				|| this.origin.getX() + getWidth() - 1 < other.origin.getX() 
				|| this.origin.getX() > other.origin.getX() + other.getWidth() - 1));
	}

	public Rectangle clone() {
		return new Rectangle(origin, width, height);
	}

}
