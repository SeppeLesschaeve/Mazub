package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public class Tile {
	
	private Rectangle rectangle;
	private Feature feature;
	
	public Tile(Position<Integer> position, int length, Feature feature) {
		this(new Rectangle(position, length, length), feature);
	}
	
	public Tile(Rectangle rectangle, Feature feature) {
		this.rectangle = rectangle;
		this.feature = feature;
	}
	
	@Basic
	public Rectangle getRectangle() {
		return this.rectangle;
	}
	
	@Basic
	public Feature getFeature() {
		return this.feature;
	}
	
	protected void setFeature(Feature feature) {
		this.feature = feature;
	}
}
