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
	
	protected HitPoint hitPoint;

	@Model
	protected Creature(int pixelLeftX, int pixelBottomY, int initialHit, int minHit, int maxHit, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, sprites);
		this.hitPoint = new HitPoint(initialHit, minHit, maxHit);
	}
	
	@Basic
	public int getHitPoints() {
		return hitPoint.getPoints();
	}
	
	protected void updateHitPoints(int points) {
		this.hitPoint.updatePoints(points);
	}

	@Basic @Override
	public int getOrientation() {
		if(kinematics.getXVelocity() < 0 || kinematics.getXAcceleration() < 0) {
			return -1;
		}else if (kinematics.getXVelocity() > 0 || kinematics.getXAcceleration() > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	protected double updateDt(double deltaT, double time) {
		double result = 0.01 / ( Math.sqrt( Math.pow(kinematics.getXVelocity(), 2) + Math.pow(kinematics.getYVelocity(), 2) ) + 
				(Math.sqrt( Math.pow(kinematics.getXAcceleration(), 2) + Math.pow(kinematics.getYAcceleration(), 2) ) * deltaT) );
		if(Double.isInfinite(result) || result > 0.01) {
			result = 0.01;
		}
		if(time + result > deltaT) {
			result = deltaT-time;
		}
		return result;
	}
	
	protected void updateX(double dt) {
		double newX = this.getPosition().getX() + (kinematics.getXVelocity()*dt)+ (kinematics.getXAcceleration()*dt*dt/2);
		super.getPosition().setX(newX);
		super.getRectangle().getOrigin().setX((int)(newX/0.01));
	}
	
	protected void updateY(double dt) {
		double newY = this.getPosition().getY() + (kinematics.getYVelocity()*dt) + (kinematics.getYAcceleration()*dt*dt/2);
		super.getPosition().setY(newY);
		this.getRectangle().getOrigin().setY((int)(newY/0.01));
	}
	
	@Override
	public boolean isDead() {
		return getHitPoints() == 0;
	}
	
	protected boolean canStopRun() {
		Rectangle endRun = new Rectangle(getRectangle().getOrigin(), getSprites()[0].getWidth(), getSprites()[0].getHeight());
		if(getWorld() != null) {
			return super.getWorld().shallBePassable(endRun) && overlappingGameObject(endRun).isEmpty();
		}
		return true;
	}
	
	protected boolean canFall() {
		Rectangle down = super.getDownBorder();
		if(getWorld() == null) return false;
		for(int x = down.getXCoordinate(); x < down.getXCoordinate() + down.getWidth(); x++)
			if(!getWorld().getTileFeature(x, down.getYCoordinate()).isPassable()) return false;
		return(overlappingGameObject(down).isEmpty());
	}

	protected boolean canRun() {
		if(getWorld()  == null) return true;
		if(getOrientation() == -1)
			return overlappingGameObject(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder());
		else if(getOrientation() == 1)
			return overlappingGameObject(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder());
		return false;
	}
	
	protected abstract boolean canJump();

}
