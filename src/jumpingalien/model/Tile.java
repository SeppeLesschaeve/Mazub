package jumpingalien.model;

import annotate.Basic;
import jumpingalien.model.feature.Feature;

public class Tile {
	
	private final Rectangle rectangle;
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
		return rectangle.copy();
	}
	
	@Basic
	public Feature getFeature() {
		return this.feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}
}
