package jumpingalien.model;

import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.model.animation.SkullcabVisualizer;
import jumpingalien.model.collision.Collidable;
import jumpingalien.model.collision.SkullcabCollider;
import jumpingalien.model.feature.FeatureHandler;
import jumpingalien.model.feature.SkullcabFeatureHandler;
import jumpingalien.model.kinematics.JumpKinematics;
import jumpingalien.model.kinematics.Kinematics;
import jumpingalien.model.storage.GameObjectStorage;
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
public class Skullcab extends Plant implements VerticalMovable {
	
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
		startJump();
	}

	@Override
	protected Collidable initializeCollider() {
		return new SkullcabCollider(this);
	}

	@Override
	protected FeatureHandler initializeFeatureHandler() {
		return new SkullcabFeatureHandler(this);
	}

	@Override
	protected Kinematics initializeKinematics() {
		return new JumpKinematics();
	}

	@Override
	protected GameObjectVisualizer initializeVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites) {
		return new SkullcabVisualizer(this, xCoordinate, yCoordinate, sprites);
	}

	@Override
	public double[] getAcceleration() {
		return new double[]{0.0, ((JumpKinematics) getKinematics()).getAcceleration()};
	}

	@Override
	public double[] getVelocity() {
		return new double[]{0.0, ((JumpKinematics) getKinematics()).getVelocity()};
	}

	@Override
	protected void performDuringTimeStep() {
		if(getMoveTime() + getTimeStep() >= Constant.PLANT_SWITCH_TIME.getValue()) {
			double remainingMoveTime = getMoveTime() + getTimeStep() - Constant.PLANT_SWITCH_TIME.getValue();
			move(Constant.PLANT_SWITCH_TIME.getValue() - getMoveTime());
			if(isJumping() && canFall()) startFall();
			else if(isFalling() && canJump()) startJump();
			move(remainingMoveTime);
		} else {
			move(getTimeStep());
		}
		setAge(getAge() + getTimeStep());
		if(isDead()){
			setDelay(getDelay() + getTimeStep());
			if(getDelay() == Constant.REMOVE_DELAY.getValue()) terminate();
		}
	}

	@Override
	public boolean isDead() {
		return super.getAge() >= 12 || super.getHitPoints() == 0;
	}

	@Override
	public int getOrientation() {
		if(isJumping()) return 1;
		if(isFalling()) return -1;
		return 0;
	}

	@Override
	public void addToStorage(GameObjectStorage worldStorage) {
		worldStorage.addSkullcab(this);
	}

	@Override
	public void removeFromStorage(GameObjectStorage worldStorage) {
		worldStorage.removeSkullcab(this);
	}

	@Override
	protected void accept(Collidable collidable) {
		collidable.collideWithSkullcab(this);
	}


	@Override
	public boolean canJump() {
		return !isDead();
	}

	@Override
	public boolean isJumping() {
		return ((JumpKinematics) getKinematics()).getVelocity() > 0;
	}

	@Override
	public boolean canFall() {
		return !isDead();
	}

	@Override
	public boolean isFalling() {
		return ((JumpKinematics) getKinematics()).getVelocity() < 0;
	}

	@Override
	public void startJump() {
		((JumpKinematics) getKinematics()).setVelocity(0.5);
	}

	@Override
	public void endJump() {
		assert(isMovingVertically() && !isDead());
		((JumpKinematics) getKinematics()).setVelocity(0.0);
	}

	@Override
	public void startFall() {
		((JumpKinematics) getKinematics()).setVelocity(-0.5);
	}
	
}
