package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;

public class Kinematics {
	
	private double XAcceleration;
	private double YAcceleration;
	private double XVelocity;
	private double YVelocity;
	private double MinXVelocity;
	private double MaxXVelocity;

	public double getXAcceleration() {
		return Double.valueOf(XAcceleration);
	}

	public double getYAcceleration() {
		return Double.valueOf(YAcceleration);
	}

	public void setXAcceleration(double x) {
		this.XAcceleration = x;
	}

	public void setYAcceleration(double y) {
		this.YAcceleration = y;
	}

	public double getXVelocity() {
		return Double.valueOf(XVelocity);
	}

	public double getYVelocity() {
		return Double.valueOf(YVelocity);
	}

	public void setXVelocity(double x) {
		this.XVelocity = x;
	}

	public void setYVelocity(double y) {
		this.YVelocity = y;
	}

	public double getMinXVelocity() {
		return Double.valueOf(MinXVelocity);
	}

	public void setMinXVelocity(double min) {
		this.MinXVelocity = min;
	}

	public double getMaxXVelocity() {
		return Double.valueOf(MaxXVelocity);
	}

	public void setMaxXVelocity(double max) {
		if(Math.abs(max) < Math.abs(getMinXVelocity())) this.MaxXVelocity = getMinXVelocity();
		else this.MaxXVelocity = max;
	}

	public void setXVelocityBounds(double min, double max) {
		this.setMinXVelocity(min);
		this.setMaxXVelocity(max);
	}
	
	public void roundXVelocity() {
		if(Math.abs(XVelocity) < Math.abs(getMinXVelocity())) {
			setXVelocity(getMinXVelocity()*Math.signum(XVelocity));
		}
		if(Math.abs(XVelocity) > Math.abs(getMaxXVelocity())) {
			setXVelocity(getMaxXVelocity()*Math.signum(XVelocity));
		}
	}

	public void updateXVelocity(double time) {
		XVelocity += XAcceleration*time;
		this.roundXVelocity();
	}

	public void updateYVelocity(double time) {
		YVelocity += YAcceleration*time;
		this.roundXVelocity();	
	}

	public boolean isStationary() {
		return XVelocity == 0 && YVelocity == 0 && XAcceleration == 0 && YAcceleration == 0;
	}
	
	public double calculateNewTimeSlice(double dt, double time) {
		double denominator = Math.sqrt( Math.pow(XVelocity, 2) + Math.pow(YVelocity, 2)) + 
				Math.sqrt( Math.pow(XAcceleration, 2) + Math.pow(YAcceleration, 2))*dt;
		double result = 0.01 / denominator;
		if(time + result > dt) {
			return dt-time;
		}
		return result;
	}
	
	
}
