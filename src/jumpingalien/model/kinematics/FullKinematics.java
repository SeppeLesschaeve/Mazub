package jumpingalien.model.kinematics;

import jumpingalien.model.GameObject;
import jumpingalien.model.VerticalMovable;
import jumpingalien.model.HorizontalMovable;

public class FullKinematics implements Kinematics{

    private final RunKinematics runKinematics;
    private final JumpKinematics jumpKinematics;

    public FullKinematics() {
        runKinematics = new RunKinematics();
        jumpKinematics = new JumpKinematics();
    }

    public double getXVelocity() {
        return runKinematics.getVelocity();
    }

    public void setXVelocity(double velocity) {
        runKinematics.setVelocity(velocity);
    }

    public double getMinXVelocity() {
        return runKinematics.getMinVelocity();
    }

    public void setMinXVelocity(double minVelocity) {
        runKinematics.setMinVelocity(minVelocity);
    }

    public double getMaxXVelocity() {
        return runKinematics.getMaxVelocity();
    }

    public void setMaxXVelocity(double maxVelocity) {
        runKinematics.setMaxVelocity(maxVelocity);
    }

    public double getXAcceleration() {
        return runKinematics.getAcceleration();
    }

    public void setXAcceleration(double acceleration) {
        runKinematics.setAcceleration(acceleration);
    }

    public void setXVelocityBounds(double minVelocity, double maxVelocity) {
        runKinematics.setVelocityBounds(minVelocity, maxVelocity);
    }

    public void updateXVelocity(double time) {
        runKinematics.updateVelocity(time);
    }

    public double getYVelocity() {
        return jumpKinematics.getVelocity();
    }

    public void setYVelocity(double velocity) {
        jumpKinematics.setVelocity(velocity);
    }

    public double getYAcceleration() {
        return jumpKinematics.getAcceleration();
    }

    public void setYAcceleration(double acceleration) {
        jumpKinematics.setAcceleration(acceleration);
    }

    public double getMaxYVelocity() {
        return jumpKinematics.getMaxVelocity();
    }

    public void setMaxYVelocity(double maxVelocity) {
        jumpKinematics.setMaxVelocity(maxVelocity);
    }

    public void updateYVelocity(double time) {
        jumpKinematics.updateVelocity(time);
    }

    public double calculateNewTimeSlice(double dt, double time) {
        double denominator =
                Math.sqrt( Math.pow(getXVelocity(), 2) + Math.pow(getYVelocity(), 2)) +
                Math.sqrt( Math.pow(getXAcceleration(), 2) + Math.pow(getYAcceleration(), 2))*dt;
        if(denominator == 0 || time +  (0.01 / denominator) > dt){
            return dt-time;
        }else{
            return 0.01 / denominator;
        }
    }

    public void updatePosition(double dt, GameObject gameObject){
        if(((HorizontalMovable)gameObject).isMovingHorizontally()){
            gameObject.setXCoordinate(gameObject.getXPosition() + (getXVelocity()*dt) + (getXAcceleration()*dt*dt/2));
            updateXVelocity(dt);
        }
        if(((VerticalMovable)gameObject).isMovingVertically()){
            gameObject.setYCoordinate(gameObject.getYPosition() + (getYVelocity()*dt) + (getYAcceleration()*dt*dt/2));
            updateYVelocity(dt);
        }
    }

    public boolean isStationary() {
        return jumpKinematics.isStationary();
    }
}
