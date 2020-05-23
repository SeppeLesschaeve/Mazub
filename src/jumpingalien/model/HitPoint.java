package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public class HitPoint {
	
	private int points;
	private int minimum;
	private int maximum;
	
	public HitPoint(int points, int minimum, int maximum) {
		if(minimum > maximum) minimum = maximum;
		this.minimum = minimum;
		this.maximum = maximum;
		this.points = points;
	}
	
	@Basic
	public int getPoints() {
		return Integer.valueOf(points);
	}
	
	protected void setPoints(int points) {
		this.points += points;
		if(this.getPoints() < this.getMinimumAmountOfPoints()) {
			this.points = minimum;
		}
		if(this.getPoints() > this.getMaximumAmountOfPoints()) {
			this.points = maximum;
		}
	}
	
	@Basic
	public int getMinimumAmountOfPoints() {
		return Integer.valueOf(minimum);
	}
	
	protected void setMinimumAmountOfPoints(int minimum) {
		if(minimum > getMaximumAmountOfPoints()) this.minimum = getMaximumAmountOfPoints();
		else this.minimum = minimum;
	}
	
	@Basic
	public int getMaximumAmountOfPoints() {
		return Integer.valueOf(maximum);
	}
	
	protected void setMaximumAmountOfPoints(int maximum) {
		if(maximum < getMinimumAmountOfPoints()) this.maximum = getMinimumAmountOfPoints();
		else this.maximum = maximum;
	}
	
	public HitPoint clone() {
		return new HitPoint(getPoints(), getMinimumAmountOfPoints(), getMaximumAmountOfPoints());
	}

}
