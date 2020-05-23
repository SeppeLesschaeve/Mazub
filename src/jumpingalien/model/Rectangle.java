package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public class Rectangle{
	
	private Position<Integer> origin;
	private Dimension dimension;
	
	public Rectangle(int x, int y, int width, int height) {
		this(new Position<Integer>(x,y), new Dimension(width, height));
	}
	
	public Rectangle(Position<Integer> origin, Dimension dimension) {
		this.origin = origin;
		this.dimension = dimension;
	}
	
	public Rectangle(int x, int y, Dimension dimension) {
		this(new Position<Integer>(x, y), dimension);
	}

	@Basic
	public Position<Integer> getOrigin() {
		return this.origin;
	}
	
	protected void setOrigin(int x, int y) {
		this.origin.setX(x);
		this.origin.setY(y);
	}
	
	@Basic
	public Dimension getDimension() {
		return this.dimension;
	}
	
	protected void setDimension(int width, int height) {
		this.dimension.setWidth(width);
		this.dimension.setHeight(height);
	}
	
	@Basic
	public boolean contains(Position<Integer> point) {
		return (origin.getX() <= point.getX() && point.getX() <= this.dimension.getWidth() - 1 && 
				origin.getY() <= point.getY() && point.getY() <= this.dimension.getHeight() - 1);
	}
	
	@Basic
	public boolean overlaps(Rectangle other) {
		return(!(this.origin.getY() + this.dimension.getHeight() - 1 < other.origin.getY() ||
				this.origin.getY() > other.origin.getY() + other.dimension.getHeight() - 1) && !(this.origin.getX() + this.dimension.getWidth() - 1 < other.origin.getX() ||
				this.origin.getX() > other.origin.getX() + other.dimension.getWidth() - 1));
	}
}
