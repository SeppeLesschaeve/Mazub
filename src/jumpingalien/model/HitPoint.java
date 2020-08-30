package jumpingalien.model;

public class HitPoint implements Cloneable{
	
	private int points;
	private final int minimum;
	private final int maximum;
	
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
		this.points = points;
	}
	
	public void updatePoints(int points) {
		this.points += points;
		if(this.points < this.minimum) {
			this.points = minimum;
		}
		if(this.points > this.maximum) {
			this.points = maximum;
		}
	}
	
	public int getMinimum() {
		return Integer.valueOf(minimum);
	}

	public int getMaximum() {
		return Integer.valueOf(maximum);
	}

	public HitPoint clone() {
		return new HitPoint(points, minimum, maximum);
	}

}
