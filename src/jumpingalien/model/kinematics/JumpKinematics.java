package jumpingalien.model.kinematics;

import jumpingalien.model.GameObject;
import jumpingalien.model.VerticalMovable;

public class JumpKinematics implements Kinematics{

    private double velocity;
    private double acceleration;
    private double maxVelocity = Double.POSITIVE_INFINITY;

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public void roundVelocity() {
        if((getVelocity() > getMaxVelocity() && getMaxVelocity() > 0) ||
                (getMaxVelocity() < 0 && getVelocity() < getMaxVelocity())) {
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
        if(((VerticalMovable)gameObject).isMovingVertically()){
            gameObject.setYCoordinate(gameObject.getYPosition() + (getVelocity()*dt) + (getAcceleration()*dt*dt/2));
            updateVelocity(dt);
        }
    }

    public boolean isStationary() {
        return getVelocity() == 0 && getAcceleration() == 0;
    }

}
