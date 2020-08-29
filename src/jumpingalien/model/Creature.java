package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Model;
import jumpingalien.util.Sprite;

/**
 * This class builds a Creature with Hit Points as health
 *  
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public abstract class Creature extends Organism implements Movable{
	
	@Model
	protected Creature(int x, int y, int initialHit, int minHit, int maxHit, Sprite... sprites){
		super(x, y, initialHit, minHit, maxHit, sprites);
	}
	
	@Override
	public boolean isDead() {
		return getPoints() == 0;
	}
	
	public void advanceTime(double deltaT) {
		if(Double.isNaN(deltaT) || deltaT < 0 || deltaT >= 0.2 || Double.isInfinite(deltaT)) throw new IllegalArgumentException();
		double dt = kinematics.calculateNewTimeSlice(deltaT, 0.0);
		for(double time = 0.0; time < deltaT; time +=dt, dt  = kinematics.calculateNewTimeSlice(deltaT, time)) {
			if(isDead()) super.setDelay(getDelay() + dt); 
			if(getDelay() >= Constant.REMOVE_DELAY.getValue()) terminate();
			if(!isDead()) {
				if(!canMoveInCurrentState()) setNewState();
				arrangeFeatureHit(dt);
				arrangeObjectHit(dt);
				arrangeMovement(dt);
			}
			if(this.isTerminated()) break;
		}
	}
	
	protected abstract void setNewState();
	protected abstract void arrangeObjectHit(double dt);
	protected abstract void arrangeFeatureHit(double dt);
	protected abstract void arrangeMovement(double dt);	

	protected abstract boolean canMoveInCurrentState();

}
