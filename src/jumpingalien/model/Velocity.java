package jumpingalien.model;
import be.kuleuven.cs.som.annotate.Basic;

public class Velocity {
	
	private double x;
	private double y;
	private double minX = 1.0;
	private double maxX = 3.0;
	private double maxY = 8.0;
	
	public Velocity(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Basic
	public double getX() {
		return x;
	}

	protected void setX(double x) {
		this.x = x;
	}
	
	@Basic
	public double getY() {
		return y;
	}

	protected void setY(double y) {
		this.y = y;
	}
	
	@Basic
	public double getMinX() {
		return minX;
	}

	protected void setMinX(double minX) {
		this.minX = minX;
	}
	
	@Basic
	public double getMaxX() {
		return maxX;
	}

	protected void setMaxX(double maxX) {
		if(maxX < minX) throw new IllegalArgumentException();
		this.maxX = maxX;
	}
	
	@Basic
	public double getMaxY() {
		return maxY;
	}
	
	protected void setMaxY(double maxY) {
		this.maxY = maxY;
	}
	
	protected void enforceBoundaries() {
		if(Math.abs(x) < minX) {
			x = minX*Math.signum(x);
		}
		if(Math.abs(x) > maxX) {
			x = maxX*Math.signum(x);
		}
		if((maxY > 0 && y > maxY) || (maxY < 0 && y < maxY)) {
			y = maxY;
		}
	}
	
	protected void setBoundsOfVelocity(double minX, double maxX) {
		this.setMinX(minX);
		this.setMaxX(maxX);
	}
	
	protected void accelerateX(Acceleration acceleration, double time) {
		x += acceleration.getX()*time;
		this.enforceBoundaries();
	}
	
	protected void accelerateY(Acceleration acceleration, double time) {
		y += acceleration.getY()*time;
		this.enforceBoundaries();
	}
	protected void accelerate(Acceleration acceleration, double time) {
		x += acceleration.getX()*time;
		y += acceleration.getY()*time;
		this.enforceBoundaries();
	}

}
