package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public class Dimension {
	
	private int width;
	private int height;
	
	public Dimension(int width, int height) throws IllegalArgumentException{
		if(width <= 0 || height <= 0) throw new IllegalArgumentException("The width and height must be strictly positive");
		setWidth(width);
		setHeight(height);
	}
	
	@Basic
	public int getWidth() {
		return Integer.valueOf(width);
	}

	protected void setWidth(int width) throws IllegalArgumentException{
		if(width <= 0) throw new IllegalArgumentException("The width must be strictly positive");
		this.width = width;
	}
	
	@Basic
	public int getHeight() {
		return Integer.valueOf(height);
	}
	
	protected void setHeight(int height) throws IllegalArgumentException{
		if(height <= 0) throw new IllegalArgumentException("The height must be strictly positive");
		this.height = height;
	}
	
	public Dimension clone() {
		return new Dimension(getWidth(), getHeight());
	}

}
