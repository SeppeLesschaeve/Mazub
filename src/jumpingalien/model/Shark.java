package jumpingalien.model;

import java.util.ArrayList;

import annotate.Basic;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.model.animation.SharkVisualizer;
import jumpingalien.model.collision.Collidable;
import jumpingalien.model.collision.SharkCollider;
import jumpingalien.model.feature.Feature;
import jumpingalien.model.feature.FeatureHandler;
import jumpingalien.model.feature.SharkFeatureHandler;
import jumpingalien.model.kinematics.FullKinematics;
import jumpingalien.model.kinematics.Kinematics;
import jumpingalien.model.kinematics.RunKinematics;
import jumpingalien.model.sharkState.LeftState;
import jumpingalien.model.sharkState.SharkState;
import jumpingalien.model.storage.GameObjectStorage;
import jumpingalien.util.Sprite;

/**
 * This class builds a shark that can run and jump 
 * the movement of this shark starts orientated to the left and will move for 0.5 seconds, 
 * then one second resting that means that the shark will not move horizontally,
 * then 0.5 seconds to the right and then again one second of rest;
 * this movement will be repeated until the shark is dead
 * 
 * @invar ...
 * 		| kinematics.getVerticalVelocity() <= getVelocity().getMaxY()
 * 
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public class Shark extends Creature implements VerticalMovable {

	private SharkState sharkState;
	private double spiderTime;
	
	/**This constructor will set the initial Pixel Position, Actual Position, Dimension and the images to show the animation
	 * 
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 * 
	 * @throws IllegalArgumentException
	 * 		| (sprites.length != 3 || for (int index = 0; index < sprites.length; index++) sprites[index] == null)
	 * 
	 * @effect ...
	 * 		|super(pixelLeftX, 100, 0, Integer.MAX_VALUE, pixelBottomY, sprites)
	 * @post ...
	 * 		|super.getAcceleration().setX(-1.5)
	 *		|super.getAcceleration().setY(Y_ACC)
	 * 
	 */
	public Shark(int pixelLeftX, int pixelBottomY, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, 100, 0, Integer.MAX_VALUE, sprites);
		if(sprites.length != 3) {
			throw new IllegalArgumentException("You can not have more or less than three sprites for a shark");
		}

	}

	public SharkState getSharkState() {
		return sharkState;
	}

	public void setSharkState(SharkState sharkState) {
		this.sharkState = sharkState;
	}

	public int getSharkStateID(){
		return sharkState.getID();
	}

	public double getSpiderTime() {
		return spiderTime;
	}

	public void setSpiderTime(double spiderTime) {
		this.spiderTime = spiderTime;
	}

	@Override
	protected Collidable initializeCollider() {
		return new SharkCollider(this);
	}

	@Override
	protected FeatureHandler initializeFeatureHandler() {
		return new SharkFeatureHandler(this);
	}

	@Override
	protected Kinematics initializeKinematics() {
		return new FullKinematics();
	}

	@Override
	protected GameObjectVisualizer initializeVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites) {
		return new SharkVisualizer(this, xCoordinate, yCoordinate, sprites);
	}

	@Override
	public double[] getAcceleration() {
		return new double[]{((FullKinematics) getKinematics()).getXAcceleration(), ((FullKinematics)getKinematics()).getYAcceleration()};
	}

	@Override
	public double[] getVelocity() {
		return new double[]{((FullKinematics) getKinematics()).getXVelocity(), ((FullKinematics)getKinematics()).getYVelocity()};
	}

	@Override
	public void advanceTime(double deltaT) {
		if(sharkState == null) this.setSharkState(new LeftState(0.0, this));
		super.advanceTime(deltaT);
	}

	@Override
	protected void performDuringTimeStep() {
		if(getWorld() != null && getWorld().getPlayer() != null) {
			if(getWorld().getPlayer().getBlockTime() != 0 && !getWorld().getPlayer().isAdvancedByWorld()){
				getWorld().getPlayer().setBlockTime(getWorld().getPlayer().getBlockTime() + getTimeStep());
			}
		}
		updateHorizontalMovement();
		updateVerticalMovement();
		if(!isDead())sharkState.move(getTimeStep());
		if(!isInside()) terminate();
		super.getFeatureHandler().handleFeature();
		super.updateImage();
		if(isDead()){
			setDelay(getDelay() + getTimeStep());
			if(getDelay() == Constant.REMOVE_DELAY.getValue()) terminate();
		}
	}

	private void updateVerticalMovement() {
		if(isJumping() && !canJump()){
			for(GameObject gameObject : getCollidingCreatures(getUpBorder())){
				gameObject.accept(super.getCollider());
			}
			if(!isDead() && isMovingVertically())endJump();
			if(canStartFall()) startFall();
		}
		if(isFalling() && !canFall()){
			for(GameObject gameObject : getCollidingCreatures(getDownBorder())){
				gameObject.accept(super.getCollider());
			}
			((FullKinematics) getKinematics()).setYVelocity(0.0);
			((FullKinematics) getKinematics()).setYAcceleration(0.0);
		}
	}

	@Override
	protected void accept(Collidable collidable) {
		collidable.collideWithShark(this);
	}


	public boolean isInWater(Rectangle rectangle) {
		if(getWorld() == null) return false;
		int tileLength = getWorld().getTileLength();
		for(int newX, pixelX = rectangle.getXCoordinate(); pixelX <= rectangle.getXCoordinate() + rectangle.getWidth() - 1; pixelX += newX) {
			for(int newY, pixelY = rectangle.getYCoordinate(); pixelY <= rectangle.getYCoordinate() + rectangle.getHeight() - 1; pixelY += newY) {
				if(getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER) return true;
				newY = Math.min(tileLength, rectangle.getYCoordinate() + rectangle.getHeight() - 1 - pixelY);
				if(newY < tileLength) newY = 1;
			}
			newX = Math.min(tileLength, rectangle.getXCoordinate() + rectangle.getWidth() - 1 - pixelX);
			if(newX < tileLength) newX = 1;
		}
		return false;
	}

	public boolean canStartJump() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return isInWater(new Rectangle(getXCoordinate(), getYCoordinate(), getImageWidth(), getImageHeight()))
				|| !super.getWorld().shallBePassable(getDownBorder());
	}

	@Override
	public boolean canJump() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getUpBorder()).isEmpty() && super.getWorld().shallBePassable(getUpBorder());
	}

	@Override
	public boolean isJumping() {
		return ((FullKinematics) getKinematics()).getYVelocity() > 0 && ((FullKinematics) getKinematics()).getYAcceleration() < 0;
	}

	public boolean canStartFall() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getDownBorder()).isEmpty()  && super.getWorld().shallBePassable(getDownBorder());
	}

	@Override
	public boolean canFall() {
		if(isTerminated()) return false;
		if(isStillNotInAGameWorld()) return true;
		if(super.getCollidingCreatures(getDownBorder()).isEmpty()
				&& super.getWorld().shallBePassable(new Rectangle(getXCoordinate(), getYCoordinate(), getImageWidth(), 1))){
			return !isInWater(new Rectangle(getXCoordinate(), getYCoordinate() + getImageHeight() - 1, getImageWidth(), 1));
		}
		return false;
	}

	@Override
	public boolean isFalling() {
		return ((FullKinematics) getKinematics()).getYVelocity() <= 0 && ((FullKinematics) getKinematics()).getYAcceleration() < 0;
	}

	@Override
	public void startJump() {
		if(!canStartJump() || isJumping()) throw new IllegalStateException("Shark can not start jumping if he did jump before it.");
		((FullKinematics) getKinematics()).setYVelocity(2.0);
		((FullKinematics) getKinematics()).setYAcceleration(-10.0);
	}

	@Override
	public void endJump() {
		if(!isMovingVertically() || isDead()) throw new IllegalStateException();
		if(((FullKinematics) getKinematics()).getYVelocity() > 0) {
			((FullKinematics) getKinematics()).setYVelocity(0.0);
		}
		((FullKinematics) getKinematics()).setYAcceleration(0.0);
		if(isFalling()) {
			((FullKinematics) getKinematics()).setYVelocity(0.0);
			((FullKinematics) getKinematics()).setYAcceleration(0.0);
		}
	}

	@Override
	public void startFall() {
		if(!canStartFall() || isFalling()) throw new IllegalStateException();
		((FullKinematics) getKinematics()).setYVelocity(0.0);
		((FullKinematics) getKinematics()).setYAcceleration(-10.0);
	}

	@Override
	public boolean canMoveRight() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder());
	}

	@Override
	public boolean isMovingRight() {
		return ((FullKinematics) getKinematics()).getXVelocity() > 0 || ((FullKinematics) getKinematics()).getXAcceleration() > 0;
	}

	@Override
	public boolean canMoveLeft() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder());
	}

	@Override
	public boolean isMovingLeft() {
		return ((FullKinematics) getKinematics()).getXVelocity() < 0 || ((FullKinematics) getKinematics()).getXAcceleration() < 0;
	}

	@Override
	public void startMoveRight() {
		assert(!isMovingHorizontally() && !isDead());
		((FullKinematics) getKinematics()).setXVelocity(0.0);
		((FullKinematics) getKinematics()).setXVelocityBounds(0.0, Double.POSITIVE_INFINITY);
		((FullKinematics) getKinematics()).setXAcceleration(1.5);
	}

	@Override
	public void endMove() {
		assert(isMovingHorizontally() &&  !isDead());
		((FullKinematics) getKinematics()).setXVelocity(0.0);
		((FullKinematics) getKinematics()).setXVelocityBounds(0.0, 0.0);
		((FullKinematics) getKinematics()).setXAcceleration(0.0);
	}

	@Override
	public void startMoveLeft() {
		assert(!isMovingHorizontally() && !isDead());
		((FullKinematics) getKinematics()).setXVelocity( 0.0);
		((FullKinematics) getKinematics()).setXVelocityBounds(0.0,Double.NEGATIVE_INFINITY);
		((FullKinematics) getKinematics()).setXAcceleration(-1.5);
	}

	@Override
	public void addToStorage(GameObjectStorage worldStorage) {
		worldStorage.addShark(this);
	}

	@Override
	public void removeFromStorage(GameObjectStorage worldStorage) {
		worldStorage.removeShark(this);
	}
}
