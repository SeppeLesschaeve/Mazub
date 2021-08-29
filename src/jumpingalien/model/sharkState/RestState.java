package jumpingalien.model.sharkState;

import jumpingalien.model.Constant;
import jumpingalien.model.Shark;
import jumpingalien.model.kinematics.FullKinematics;

public class RestState implements SharkState{

    private double restTime;
    private Shark shark;
    private int previousOrientation;


    public RestState(double time, Shark shark, int previousOrientation) {
        this.restTime = time;
        this.shark = shark;
        this.previousOrientation = previousOrientation;
        initializeMovement();
        move(time);
    }

    @Override
    public double getTime() {
        return restTime;
    }

    @Override
    public void setTime(double time) {
        this.restTime = time;
    }

    @Override
    public void next(double time) {
        if(previousOrientation == -1 ){
            this.shark.setSharkState(new RightState(time, shark));
        }else if(previousOrientation == 1){
            this.shark.setSharkState(new LeftState(time, shark));
        }
    }

    @Override
    public void move(double time) {
        if(getTime() + time < Constant.SHARK_REST_TIME.getValue()){
            setTime(getTime() + time);
            shark.getKinematics().updatePosition(time, shark);
        }else{
            shark.getKinematics().updatePosition(Constant.SHARK_REST_TIME.getValue() - getTime(), shark);
            next(getTime() + time - Constant.SHARK_REST_TIME.getValue());
            this.restTime = 0.0;
        }
    }

    @Override
    public void initializeMovement() {
        ((FullKinematics) shark.getKinematics()).setXAcceleration(0.0);
        ((FullKinematics) shark.getKinematics()).setXVelocity(0.0);
    }

    @Override
    public int getID() {
        return 0;
    }
}
