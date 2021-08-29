package jumpingalien.model;

import java.util.ArrayList;

import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.model.animation.SpiderVisualizer;
import jumpingalien.model.collision.Collidable;
import jumpingalien.model.collision.SpiderCollider;
import jumpingalien.model.feature.Feature;
import jumpingalien.model.feature.FeatureHandler;
import jumpingalien.model.feature.SpiderFeatureHandler;
import jumpingalien.model.kinematics.FullKinematics;
import jumpingalien.model.kinematics.Kinematics;
import jumpingalien.model.storage.GameObjectStorage;
import jumpingalien.util.Sprite;

/**
 * This class builds a spider that can run and jump
 * and with legs that can increase or decrease in amount because of collision with features and other Game Object
 *  
 * @invar ...
 * 		| kinematics.getVerticalVelocity() <= getVelocity().getMaxY()
 *
 * @author Seppe Lesschaeve (Informatica)
 * @version 1.0
 *
 */
public class Spider extends Creature implements VerticalMovable {
	
	/**
	 * 
	 * This constructor will set the Legs, Pixel Position, Actual Position, Dimension and the images to show the animation
	 * 
	 * @param nbLegs
	 * 			This parameter is used as the number of legs
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as the images to show the animation
	 * 
	 * @throws IllegalArgumentException
	 * 		...
	 * 		| sprites.length != 3 || nbLegs < 0
	 * @effect ...
	 * 	  	| super(pixelLeftX, pixelBottomY, sprites)
	 * @post ...
	 * 		| this.legs = new Legs(0, nbLegs)
	 */
	public Spider(int nbLegs, int pixelLeftX, int pixelBottomY, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, nbLegs, 0, Integer.MAX_VALUE, sprites);
		if(sprites.length != 3 || nbLegs < 0) throw new IllegalArgumentException();
	}

	@Override
	protected Collidable initializeCollider() {
		return new SpiderCollider(this);
	}

	@Override
	protected FeatureHandler initializeFeatureHandler() {
		return new SpiderFeatureHandler(this);
	}

	@Override
	protected Kinematics initializeKinematics() {
		return new FullKinematics();
	}

	@Override
	protected GameObjectVisualizer initializeVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites) {
		return new SpiderVisualizer(this, xCoordinate, yCoordinate, sprites);
	}

	@Override
	public double[] getAcceleration() {
		return new double[]{((FullKinematics) getKinematics()).getXAcceleration(), ((FullKinematics)getKinematics()).getYAcceleration()};
	}

	@Override
	public double[] getVelocity() {
		return new double[]{((FullKinematics) getKinematics()).getXVelocity(), ((FullKinematics)getKinematics()).getYVelocity()};
	}

	/**
	 * This method is used to indicate whether the spider is considered dead
	 * 
	 * @return ...
	 * 		| result == getLegs() < 2
	 */
	@Override
	public boolean isDead() {
		return getHitPoints() < 2;
	}

	@Override
	protected void accept(Collidable collidable) {
		collidable.collideWithSpider(this);
	}

	@Override
	public int getOrientation() {
		if(isJumping() || isMovingRight()) return 1;
		if(isFalling() || isMovingLeft()) return -1;
		return 0;
	}

	@Override
	public void addToStorage(GameObjectStorage worldStorage) {
		worldStorage.addSpider(this);
	}

	@Override
	public void removeFromStorage(GameObjectStorage worldStorage) {
		worldStorage.removeSpider(this);
	}

	public boolean isInContactWithFeature(Feature feature, Rectangle rect) {
		if(getWorld() == null) {return false;}
		int tileLength = getWorld().getTileLength();
		for(int newX, pixelX = rect.getXCoordinate(); pixelX <= rect.getXCoordinate() + rect.getWidth() - 1; pixelX += newX) {
			for(int newY, pixelY = rect.getYCoordinate(); pixelY <= rect.getYCoordinate() + rect.getHeight() - 1; pixelY += newY) {
				if(getWorld().getTileFeature(pixelX, pixelY) == feature) return true;
				newY = Math.min(tileLength, rect.getYCoordinate() + rect.getHeight() - 1 - pixelY);
				if(newY < tileLength) newY = 1;
			}
			newX = Math.min(tileLength, rect.getXCoordinate() + rect.getWidth() - 1 - pixelX);
			if(newX < tileLength) newX = 1;
		}
		return false;
	}

	@Override
	public void advanceTime(double deltaT) {
		arrangeInitialMovement();
		super.advanceTime(deltaT);
	}

	private void arrangeInitialMovement() {
		if(((FullKinematics) getKinematics()).getXVelocity() == 0.0 && canMoveLeft()) {
			startMoveLeft();
		}else if(((FullKinematics) getKinematics()).getXVelocity() == 0.0 && canMoveRight()) {
			startMoveRight();
		}
		if(((FullKinematics) getKinematics()).getYVelocity() == 0.0 && canJump()) {
			startJump();
		}else if(((FullKinematics) getKinematics()).getYVelocity() == 0.0 && canFall()) {
			startFall();
		}
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
		getKinematics().updatePosition(getTimeStep(), this);
		if(!isInside()) terminate();
		super.getFeatureHandler().handleFeature();
		super.updateImage();
		if(isDead()){
			setDelay(getDelay() + getTimeStep());
			if(getDelay() == Constant.REMOVE_DELAY.getValue()) terminate();
		}
	}

	public void updateHorizontalMovement() {
		if(isMovingRight() && !canMoveRight()){
			for(GameObject gameObject : getCollidingCreatures(getRightBorder())){
				gameObject.accept(super.getCollider());
			}
			if(isMovingRight() && !isDead()) endMove();
		}
		if(isMovingLeft() && !canMoveLeft()){
			for(GameObject gameObject : getCollidingCreatures(getLeftBorder())){
				gameObject.accept(super.getCollider());
			}
			if(isMovingLeft() && !isDead())endMove();
		}
	}

	private void updateVerticalMovement() {
		if(isJumping() && !canJump()){
			for(GameObject gameObject : getCollidingCreatures(getUpBorder())){
				gameObject.accept(super.getCollider());
			}
			if(isJumping() && !isDead())endJump();
		}
		if(isFalling() && !canFall()){
			for(GameObject gameObject : getCollidingCreatures(getDownBorder())){
				gameObject.accept(super.getCollider());
			}
			if(isFalling() && !isDead())endJump();
		}
	}

	@Override
	public boolean canJump() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return getCollidingCreatures(getUpBorder()).isEmpty() && super.getWorld().shallBePassable(getUpBorder())
				&& (!super.getWorld().shallBePassable(getLeftBorder()) || !super.getWorld().shallBePassable(getRightBorder()));
	}

	@Override
	public boolean isJumping() {
		return ((FullKinematics) getKinematics()).getYVelocity() > 0;
	}

	@Override
	public boolean canFall() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return getCollidingCreatures(getDownBorder()).isEmpty() && super.getWorld().shallBePassable(getDownBorder())
				&& (!super.getWorld().shallBePassable(getLeftBorder()) || !super.getWorld().shallBePassable(getRightBorder()));
	}

	@Override
	public boolean isFalling() {
		return ((FullKinematics) getKinematics()).getYVelocity() < 0;
	}

	@Override
	public void startJump() {
		if(isJumping() || isDead()) throw new IllegalStateException("Spider can not start jumping if he did jump before it.");
		((FullKinematics) getKinematics()).setYVelocity(1.0);
		((FullKinematics) getKinematics()).setYAcceleration(0.5);
		((FullKinematics) getKinematics()).setMaxYVelocity(1.0 + 0.5*0.5*getHitPoints());
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
		if(isFalling() || isDead()) throw new IllegalStateException("Spider can not start jumping if he did jump before it.");
		((FullKinematics) getKinematics()).setYVelocity(-1.0);
		((FullKinematics) getKinematics()).setYAcceleration(-0.5);
		((FullKinematics) getKinematics()).setMaxYVelocity(-1.0 - 0.5*0.5*getHitPoints());
	}

	@Override
	public boolean canMoveRight() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return getCollidingCreatures(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder())
				&& (!super.getWorld().shallBePassable(getUpBorder()) || !super.getWorld().shallBePassable(getDownBorder()));
	}

	@Override
	public boolean isMovingRight() {
		return ((FullKinematics) getKinematics()).getXVelocity() > 0;
	}

	@Override
	public boolean canMoveLeft() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return getCollidingCreatures(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder()) &&
				(!super.getWorld().shallBePassable(getUpBorder()) || !super.getWorld().shallBePassable(getDownBorder()));
	}

	@Override
	public boolean isMovingLeft() {
		return ((FullKinematics) getKinematics()).getXVelocity() < 0;
	}

	@Override
	public void startMoveRight() {
		assert(!isMovingRight() && !isDead());
		((FullKinematics) getKinematics()).setXVelocity(0.15*getHitPoints());
	}

	@Override
	public void endMove() {
		assert(isMovingHorizontally() &&  !isDead());
		((FullKinematics) getKinematics()).setXVelocity(0.0);
	}

	@Override
	public void startMoveLeft() {
		assert(!isMovingLeft() && !isDead());
		((FullKinematics) getKinematics()).setXVelocity(-0.15*getHitPoints());
	}
}
