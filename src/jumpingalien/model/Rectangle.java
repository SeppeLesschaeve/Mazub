package jumpingalien.model;

import annotate.Basic;

public class Rectangle{
	
	private Position<Integer> origin;
	private int width;
	private int height;
	
	public Rectangle(int x, int y, int width, int height) {
		this(new Position<>(x,y), width, height);
	}
	
	public Rectangle(Position<Integer> origin, int width, int height) {
		this.origin = origin;
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public int getXCoordinate() {
		return this.origin.getXCoordinate();
	}

	public void setXCoordinate(int xCoordinate) {
		this.origin.setXCoordinate(xCoordinate);
	}

	public int getYCoordinate() {
		return this.origin.getYCoordinate();
	}

	public void setYCoordinate(int yCoordinate) {
		this.origin.setYCoordinate(yCoordinate);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if(width <= 0) throw new IllegalArgumentException("The width must be strictly positive");
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if(height <= 0) throw new IllegalArgumentException("The height must be strictly positive");
		this.height = height;
	}

	@Basic
	public boolean contains(Position<Integer> point) {
		return getXCoordinate() <= point.getXCoordinate() && point.getXCoordinate() <= getWidth() - 1 &&
				getYCoordinate() <= point.getYCoordinate() && point.getYCoordinate() <= getHeight() - 1;
	}
	
	@Basic
	public boolean overlaps(Rectangle other) {
		return getXCoordinate() + width - 1 >= other.getXCoordinate()
				&& other.getXCoordinate() + other.getWidth() - 1 >= getXCoordinate()
				&& getYCoordinate() + height -1 >= other.getYCoordinate()
				&& other.getYCoordinate() + other.getHeight() - 1 >= getYCoordinate();
	}

	public Rectangle copy() {
		return new Rectangle(origin.copy(), width, height);
	}

    public boolean contains(int xCoordinate, int yCoordinate) {
		return contains(new Position<>(xCoordinate, yCoordinate));
    }
}
