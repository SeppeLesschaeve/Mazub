package jumpingalien.model.kinematics;

import jumpingalien.model.GameObject;
import jumpingalien.model.HorizontalMovable;

public class RunKinematics implements Kinematics{

    private double velocity;
    private double minVelocity = Double.NEGATIVE_INFINITY;
    private double maxVelocity = Double.POSITIVE_INFINITY;
    private double acceleration;

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getMinVelocity() {
        return minVelocity;
    }

    public void setMinVelocity(double minVelocity) {
        this.minVelocity = minVelocity;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void setVelocityBounds(double minVelocity, double maxVelocity) {
        if(minVelocity > maxVelocity){
            setMinVelocity(maxVelocity);
            setMaxVelocity(minVelocity);
        }else{
            setMinVelocity(minVelocity);
            setMaxVelocity(maxVelocity);
        }
    }

    public void roundVelocity() {
        if(getVelocity() < getMinVelocity()) {
            setVelocity(getMinVelocity());
        }
        if(getVelocity() > getMaxVelocity()) {
            setVelocity(getMaxVelocity());
        }
    }

    public void updateVelocity(double time) {
        setVelocity(getVelocity() + getAcceleration()*time);
        roundVelocity();
    }

    public double calculateNewTimeSlice(double dt, double time) {
        double denominator = Math.sqrt( Math.pow(getVelocity(), 2)) + Math.sqrt( Math.pow(getAcceleration(),2))*dt;
        if(denominator == 0 || time +  (0.01 / denominator) > dt){
            return dt-time;
        }else{
            return 0.01 / denominator;
        }
    }

    public void updatePosition(double dt, GameObject gameObject){
        if(((HorizontalMovable)gameObject).isMovingHorizontally()){
            gameObject.setXCoordinate(gameObject.getXPosition() + (getVelocity()*dt) + (getAcceleration()*dt*dt/2));
            updateVelocity(dt);
        }
    }

    public boolean isStationary() {
        return getVelocity() == 0 && getAcceleration() == 0;
    }
}
