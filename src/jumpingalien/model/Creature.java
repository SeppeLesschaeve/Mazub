package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import jumpingalien.util.Sprite;

/**
 * This class builds a Creature with Hit Points as health
 *  
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public abstract class Creature extends Organism {
	
	@Model
	protected Creature(int x, int y, int initialHit, int minHit, int maxHit, Sprite... sprites){
		super(x, y, initialHit, minHit, maxHit, sprites);
	}
	

	@Basic @Override
	public int getOrientation() {
		if(kinematics.getXVelocity() < 0 || kinematics.getXAcceleration() < 0) return -1;
		else if (kinematics.getXVelocity() > 0 || kinematics.getXAcceleration() > 0) return 1;
		else return 0;
	}
	
	protected void updateX(double dt) {
		super.updateHorizontalComponent(this.getPosition().getX() + (kinematics.getXVelocity()*dt)+ (kinematics.getXAcceleration()*dt*dt/2));
	}
	
	protected void updateY(double dt) {
		super.updateVerticalComponent(this.getPosition().getY() + (kinematics.getYVelocity()*dt) + (kinematics.getYAcceleration()*dt*dt/2));
	}
	
	@Override
	public boolean isDead() {
		return getPoints() == 0;
	}
	
	protected boolean canRun() {
		if(getWorld()  == null) return true;
		if(getOrientation() == -1)
			return overlappingGameObject(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder());
		else if(getOrientation() == 1)
			return overlappingGameObject(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder());
		return true;
	}
	
	public void advanceTime(double deltaT) {
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT >= 0.2 || Double.isInfinite(deltaT)) throw new IllegalArgumentException();
		arrangeInitialMovement();
		double dt = kinematics.calculateNewTimeSlice(deltaT, 0.0);
		for(double time = 0.0; time < deltaT; time +=dt, dt  = kinematics.calculateNewTimeSlice(deltaT, time)) {
			if(isDead()) super.setDelay(getDelay() + dt); 
			if(getDelay() >= Constant.REMOVE_DELAY.getValue()) terminate();
			arrangeFeatureHit(dt);
			arrangeObjectHit(dt);
			if(!canMoveInCurrentState()) setNewState();
			arrangeMovement(dt);
		}
	}
	
	protected abstract void arrangeMovement(double dt);
	protected abstract void arrangeObjectHit(double dt);
	protected abstract void arrangeFeatureHit(double dt);
	protected abstract void arrangeInitialMovement();
	protected abstract void setNewState();	

	protected abstract boolean canJump();
	protected abstract boolean canMoveInCurrentState();

}
