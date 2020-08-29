package jumpingalien.model;


public class Kinematics {
	
	private double xAcceleration;
	private double yAcceleration;
	private double xVelocity;
	private double yVelocity;
	private double minXVelocity = Double.MIN_VALUE;
	private double maxXVelocity = Double.MAX_VALUE;
	private double maxYVelocity = Double.MAX_VALUE;

	public double getXAcceleration() {
		return Double.valueOf(xAcceleration);
	}

	public double getYAcceleration() {
		return Double.valueOf(yAcceleration);
	}

	public void setXAcceleration(double x) {
		this.xAcceleration = x;
	}

	public void setYAcceleration(double y) {
		this.yAcceleration = y;
	}

	public double getXVelocity() {
		return Double.valueOf(xVelocity);
	}

	public double getYVelocity() {
		return Double.valueOf(yVelocity);
	}

	public void setXVelocity(double x) {
		this.xVelocity = x;
	}

	public void setYVelocity(double y) {
		this.yVelocity = y;
	}

	public double getMinXVelocity() {
		return Double.valueOf(minXVelocity);
	}

	public void setMinXVelocity(double min) {
		this.minXVelocity = min;
	}

	public double getMaxXVelocity() {
		return Double.valueOf(maxXVelocity);
	}

	public void setMaxXVelocity(double max) {
		if(Math.abs(max) < Math.abs(minXVelocity)) this.maxXVelocity = minXVelocity;
		else this.maxXVelocity = max;
	}

	public double getMaxYVelocity() {
		return maxYVelocity;
	}

	public void setMaxYVelocity(double max) {
		this.maxYVelocity = max;
	}

	public void setXVelocityBounds(double min, double max) {
		this.setMinXVelocity(min);
		this.setMaxXVelocity(max);
	}
	
	public void roundXVelocity() {
		if(Math.abs(xVelocity) < Math.abs(minXVelocity)) {
			setXVelocity(minXVelocity*Math.signum(xVelocity));
		}
		if(Math.abs(xVelocity) > Math.abs(maxXVelocity)) {
			setXVelocity(maxXVelocity*Math.signum(xVelocity));
		}
	}

	public void updateXVelocity(double time) {
		xVelocity += xAcceleration*time;
		this.roundXVelocity();
	}

	public void roundYVelocity() {
		if(Math.abs(yVelocity) > Math.abs(maxYVelocity)) {
			setYVelocity(maxYVelocity*Math.signum(yVelocity));
		}
	}
	
	public void updateYVelocity(double time) {
		yVelocity += yAcceleration*time;
		this.roundYVelocity();	
	}

	public boolean isStationary() {
		return xVelocity == 0 && yVelocity == 0 && xAcceleration == 0 && yAcceleration == 0;
	}
	
	public double calculateNewTimeSlice(double dt, double time) {
		double denominator = Math.sqrt( Math.pow(xVelocity, 2) + Math.pow(yVelocity, 2)) + 
				Math.sqrt( Math.pow(xAcceleration, 2) + Math.pow(yAcceleration, 2))*dt;
		double result = 0.01 / denominator;
		if(Double.isInfinite(result) || result > 0.01) result = 0.01;
		if(time + result > dt) {
			return dt-time;
		}
		return result;
	}
	
	
}
