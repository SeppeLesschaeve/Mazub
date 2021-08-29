package jumpingalien.model;

import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.model.animation.SneezewortVisualizer;
import jumpingalien.model.collision.Collidable;
import jumpingalien.model.collision.SneezewortCollider;
import jumpingalien.model.feature.FeatureHandler;
import jumpingalien.model.feature.SneezewortFeatureHandler;
import jumpingalien.model.kinematics.Kinematics;
import jumpingalien.model.kinematics.RunKinematics;
import jumpingalien.model.storage.GameObjectStorage;
import jumpingalien.util.Sprite;

/**
 * This class builds a Sneezewort that can run at constant velocity
                     
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 * 
 */
public class Sneezewort extends Plant implements HorizontalMovable {

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
		startMoveLeft();
	}

	@Override
	protected Collidable initializeCollider() {
		return new SneezewortCollider(this);
	}

	@Override
	protected FeatureHandler initializeFeatureHandler() {
		return new SneezewortFeatureHandler(this);
	}

	@Override
	protected Kinematics initializeKinematics() {
		return new RunKinematics();
	}

	@Override
	protected GameObjectVisualizer initializeVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites) {
		return new SneezewortVisualizer(this, xCoordinate, yCoordinate, sprites);
	}

	@Override
	public double[] getAcceleration() {
		return new double[]{((RunKinematics) getKinematics()).getAcceleration(), 0.0};
	}

	@Override
	public double[] getVelocity() {
		return new double[]{((RunKinematics) getKinematics()).getVelocity(), 0.0};
	}

	@Override
	protected void performDuringTimeStep() {
		if(getMoveTime() + getTimeStep() >= Constant.PLANT_SWITCH_TIME.getValue()) {
			double remainingMoveTime = getMoveTime() + getTimeStep() - Constant.PLANT_SWITCH_TIME.getValue();
			move(Constant.PLANT_SWITCH_TIME.getValue() - getMoveTime());
			if(isMovingRight() && canMoveLeft()) startMoveLeft();
			else if(isMovingLeft() && canMoveRight()) startMoveRight();
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
		return super.getAge() >= 10 || super.getHitPoints() == 0;
	}

	@Override
	public int getOrientation() {
		if(isMovingRight()) return 1;
		if(isMovingLeft()) return -1;
		return 0;
	}

	@Override
	public void addToStorage(GameObjectStorage worldStorage) {
		worldStorage.addSneezewort(this);
	}

	@Override
	public void removeFromStorage(GameObjectStorage worldStorage) {
		worldStorage.removeSneezewort(this);
	}

	@Override
	protected void accept(Collidable collidable) {
		collidable.collideWithSneezewort(this);
	}


	@Override
	public boolean canMoveRight() {
		return !isDead();
	}

	@Override
	public boolean isMovingRight() {
		return ((RunKinematics) getKinematics()).getVelocity() > 0;
	}

	@Override
	public boolean canMoveLeft() {
		return !isDead();
	}

	@Override
	public boolean isMovingLeft() {
		return ((RunKinematics) getKinematics()).getVelocity() < 0;
	}

	@Override
	public void startMoveRight() {
		((RunKinematics) getKinematics()).setVelocity(0.5);
	}

	@Override
	public void endMove() {
		assert(isMovingHorizontally() && !isDead());
		((RunKinematics) getKinematics()).setVelocity(0.0);
	}

	@Override
	public void startMoveLeft() {
		((RunKinematics) getKinematics()).setVelocity(-0.5);
	}

		
}