package jumpingalien.model;

import jumpingalien.util.Sprite;

/**
 * This class builds a Sneezewort that can run at constant velocity
                     
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 * 
 */
public class Sneezewort extends Plant implements OnlyHorizontalMovable{
	
	
	/**
	 * This constructor will set the initial Pixel Position, Actual Position, HitPoint and the images to show the animation
	 * 
	 * @param xPixelLeft
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param yPixelBottom
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 * 
	 * @throws IllegalArgumentException
	 * 		...
	 * 		| (sprites.length != 2)
	 * @effect ...
	 * 		| super(pixelLeftX, pixelBottomY, 1, sprites)
	 * @post ...
	 * 		| startMove()
	 * 
	 */
	public Sneezewort(int xPixelLeft, int yPixelBottom, Sprite... sprites){
		super(xPixelLeft, yPixelBottom, 1, sprites);
		if(sprites.length != 2) throw new IllegalArgumentException("You must have exactly two images");
		kinematics.setXVelocity(-0.5);
	}
	

	@Override
	public boolean isDead() {
		return super.getAge() >= 10 || super.getPoints() == 0;
	}
	
	@Override
	protected void arrangeEat(double dt) {
		super.updateHitPoints(-1);
		terminate();
	}

	/**
	 * This method will update the position using deltaT, velocity and the current sprite when not dead 
	 * 
	 * @param deltaT
	 * 			This parameter is used as the time to update the position
	 * @post ...
	 * 		| setTimer(getTimer() + deltaT) && run(deltaT)
	 * @post ...
	 *		|if(!super.isInside()) then terminate() && return
	 * @post ...
	 *		|if(getWorld() != null && getWorld().getPlayer() != null) then getWorld().handleImpact(getWorld().getPlayer(), this, deltaT)
	 * 
	 */
	@Override
	protected void arrangeMove(double deltaT) {
		if(getMoveTime() >= Constant.PLANT_SWITCH_TIME.getValue()) {
			if(isRunningRight()) startRunLeft();
			else if(isRunningLeft()) startRunRight();
		}
		else run(deltaT);
		if(!super.isInside()) terminate();
	}

	@Override
	public boolean canStartRunRight() {
		return !isDead();
	}

	@Override
	public boolean canRunRight() {
		return !isDead();
	}

	@Override
	public boolean isRunningRight() {
		return kinematics.getXVelocity() > 0;
	}

	@Override
	public boolean canStartRunLeft() {
		return !isDead();
	}

	@Override
	public boolean canRunLeft() {
		return !isDead();
	}

	@Override
	public boolean isRunningLeft() {
		return kinematics.getXVelocity() < 0;
	}

	@Override
	public void startRunRight() {
		kinematics.setXVelocity(0.5);
		super.setSprite(1);
		super.setMoveTime(0);
	}

	@Override
	public void endRunRight() {
		kinematics.setXVelocity(0.0);
	}

	@Override
	public void startRunLeft() {
		kinematics.setXVelocity(-0.5);
		super.setSprite(0);
		super.setMoveTime(0);
	}

	@Override
	public void endRunLeft() {
		kinematics.setXVelocity(0.0);
	}

	@Override
	public void run(double deltaT) {
		super.setMoveTime(getMoveTime()+deltaT);
		super.updateHorizontalComponent(this.getPosition().getX() + (kinematics.getXVelocity()*deltaT) + (kinematics.getXAcceleration()*deltaT*deltaT/2));
		kinematics.updateXVelocity(deltaT);
	}
		
}