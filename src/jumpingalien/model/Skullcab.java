package jumpingalien.model;

import jumpingalien.util.Sprite;

/**
 * This class builds a Skullcab that can jump at constant velocity
 * 
 * @invar ...
 * 		| kinematics.getVerticalVelocity() <= getVelocity().getMaxY()
 * 
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public class Skullcab extends Plant implements OnlyVerticalMovable{
	
	/**
	 * This constructor will set the initial Pixel Position, Actual Position, HitPoint and the images to show the animation
	 * 
	 * @param xPixelLeft
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param yBottomLeft
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 *  
	 *  @throws IllegalArgumentException
	 * 		...
	 * 		| (sprites.length != 2)
	 * @effect ...
	 * 		| super(xPixelLeft, yBottomLeft, 3, sprites)
	 * @post ...
	 * 		| startMove()
	 * 
	 */
	public Skullcab(int xPixelLeft, int yBottomLeft, Sprite... sprites){
		super(xPixelLeft, yBottomLeft, 3, sprites);
		if(sprites.length != 2) throw new IllegalArgumentException("You must have exactly two images");
		kinematics.setYVelocity(0.5);
	}
	
	@Override
	public boolean isDead() {
		return super.getAge() >= 12 || super.getPoints() == 0;
	}

	@Override
	protected void arrangeEat(double dt) {
		super.updateHitPoints(-1);
		if(isDead()) terminate();
	}
	
	/**
	 * This method will update the position using deltaT, velocity and the current sprite when not dead 
	 * 
	 * @param deltaT
	 * 			This parameter is used as the time to update the position
	 * @post ...
	 * 		| setTimer(getTimer() + deltaT) && jump(deltaT)
	 * @post ...
	 *		|if(!super.isInside()) then terminate() && return
	 * @post ...
	 *		|if(getWorld() != null && getWorld().getPlayer() != null) then getWorld().handleImpact(getWorld().getPlayer(), this, deltaT)
	 * 
	 */
	@Override
	protected void arrangeMove(double deltaT){
		if(getMoveTime() >= Constant.PLANT_SWITCH_TIME.getValue()) {
			if(isGoingUp()) startFall();
			else if(isGoingDown()) startJump();
		}else jump(deltaT);
		if(!super.isInside()) terminate();
	}

	@Override
	public boolean canStartJump() {
		return !isDead();
	}

	@Override
	public boolean canJump() {
		return !isDead();
	}

	@Override
	public boolean isGoingUp() {
		return kinematics.getYVelocity() > 0;
	}

	@Override
	public boolean canStartFall() {
		return !isDead();
	}

	@Override
	public boolean canFall() {
		return !isDead();
	}

	@Override
	public boolean isGoingDown() {
		return kinematics.getYVelocity() < 0;
	}

	@Override
	public void startJump() {
		kinematics.setYVelocity(0.5);
		super.setSprite(0);
		super.setMoveTime(0);
	}

	@Override
	public void endGoingUp() {
		kinematics.setYVelocity(0.0);
	}

	@Override
	public void startFall() {
		kinematics.setYVelocity(-0.5);
		super.setSprite(1);
		super.setMoveTime(0);
	}

	@Override
	public void endGoingDown() {
		kinematics.setYVelocity(0.0);
	}

	@Override
	public void jump(double deltaT) {
		super.setMoveTime(getMoveTime()+deltaT);
		super.updateVerticalComponent(this.getPosition().getY() + (kinematics.getYVelocity()*deltaT) + (kinematics.getYAcceleration()*deltaT*deltaT/2));
		kinematics.updateYVelocity(deltaT);
	}

	
}
