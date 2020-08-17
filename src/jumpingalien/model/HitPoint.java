package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public class HitPoint {
	
	private int points;
	private int minimum = Integer.MIN_VALUE;
	private int maximum = Integer.MAX_VALUE;
	
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
		if(this.getPoints() < this.getMinimum()) {
			this.points = minimum;
		}
		if(this.getPoints() > this.getMaximum()) {
			this.points = maximum;
		}
	}
	
	public int getMinimum() {
		return Integer.valueOf(minimum);
	}

	public void setMinimum(int minimum) {
		if(minimum > getMaximum()) this.minimum = getMaximum();
		else this.minimum = minimum;
	}

	public int getMaximum() {
		return Integer.valueOf(maximum);
	}

	public void setMaximum(int maximum) {
		if(maximum < getMinimum()) this.maximum = getMinimum();
		else this.maximum = maximum;
	}

}
