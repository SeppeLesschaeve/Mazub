package jumpingalien.model.sharkState;

import jumpingalien.model.Constant;
import jumpingalien.model.Shark;

public class LeftState implements SharkState{

    private double moveTime;
    private Shark shark;

    public LeftState(double time, Shark shark) {
        this.moveTime = time;
        this.shark = shark;
        initializeMovement();
        move(time);
    }

    @Override
    public double getTime() {
        return moveTime;
    }

    @Override
    public void setTime(double time) {
        this.moveTime = time;
    }

    @Override
    public void next(double time) {
        shark.setSharkState(new RestState(time, shark, -1));
    }

    @Override
    public void move(double time) {
        if(getTime() + time < Constant.SHARK_SWITCH_TIME.getValue()){
            setTime(getTime() + time);
            shark.getKinematics().updatePosition(time, shark);
        }else{
            shark.getKinematics().updatePosition(Constant.SHARK_SWITCH_TIME.getValue() - getTime(), shark);
            next(getTime() + time - Constant.SHARK_SWITCH_TIME.getValue());
            this.moveTime = 0.0;
        }
    }

    @Override
    public void initializeMovement() {
        if(this.shark.getKinematics().isStationary() && this.shark.canStartJump()) {
            shark.startJump();
        }else if(this.shark.getKinematics().isStationary() && this.shark.canStartFall()){
            shark.startFall();
        }
        if(!shark.isMovingHorizontally() && shark.canMoveLeft())shark.startMoveLeft();
    }

    @Override
    public int getID() {
        return 1;
    }
}
