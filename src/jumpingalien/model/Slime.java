package jumpingalien.model;

import java.util.ArrayList;
import java.util.Set;

import annotate.Basic;
import annotate.Raw;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.model.animation.SlimeVisualizer;
import jumpingalien.model.collision.Collidable;
import jumpingalien.model.collision.SlimeCollider;
import jumpingalien.model.feature.FeatureHandler;
import jumpingalien.model.feature.SlimeFeatureHandler;
import jumpingalien.model.kinematics.Kinematics;
import jumpingalien.model.kinematics.RunKinematics;
import jumpingalien.model.storage.GameObjectStorage;
import jumpingalien.util.Sprite;

/**
 * This class builds a slime that is able to run BUT NOT TO JUMP
 * 
 * @invar ...
 * 		| hasProperSchool()
 * @invar ...
 * 		| getVelocity().getX() <= getVelocity().getMaxX())
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 * 
 */
public class Slime extends Creature {
	
	private School school;
	private double spiderTime;
	private long id;

	/**
	 * This constructor will set the initial Pixel Position, Actual Position, HitPoints and the images to show the animation
	 * 
	 * @param ids 
	 * 			This set is used to check if it does not contain the id 
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param id
	 * 			This parameter is used as id
	 * @param school
	 * 			This parameter is used as school
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 * 
	 * @throws IllegalArgumentException
	 * 			...
	 * 		| sprites.length != 2 || id < 0 || ids.contains(id)
	 * @effect ...
	 * 		| super(pixelLeftX, pixelBottomY, 100, 0, Integer.MAX_VALUE, sprites)
	 * @post ...
	 * 		| this.setId(id) && if(getSchool() != null) then getSchool().addSlime(this)
	 * @post ...
	 * 		|	if(school != null) then school.addSlime(this)
	 * @post ...
	 * 		| super.getAcceleration().setX(0.7) && super.getAcceleration().setY(0.0) && 
	 * 		| super.getVelocity().setMinX(0.0) && super.getVelocity().setMaxX(X_MAX_VELOCITY)
	 * 
	 */
	public Slime(Set<Long> ids, int pixelLeftX, int pixelBottomY, long id, School school, Sprite... sprites){
		super(pixelLeftX, pixelBottomY, 100, 0, Integer.MAX_VALUE, sprites);
		if(sprites.length != 2 || id < 0 || ids.contains(id)) throw new IllegalArgumentException("You must have exactly two images");
		this.setId(id);
		this.startMoveRight();
		if(school != null) {
			school.addSlime(this);
		}
	}

	public double getSpiderTime() {
		return spiderTime;
	}

	public void setSpiderTime(double spiderTime) {
		this.spiderTime = spiderTime;
	}

	/**
	 * This method returns the id
	 * 
	 * @return ...
	 * 		|result == id
	 * 
	 */
	@Basic
	public long getId() {
		return id;
	}
	
	/**
	 * This method is used to set the id
	 * 
	 * @param id
	 * 		This parameter is used as the id
	 * 
	 * @post ...
	 * 		|(new this).id = id
	 * 
	 */
	private void setId(long id) {
		this.id = id;
	}
	
	/**
	 * This method is used to clear the id
	 * 
	 * @effect ...
	 * 		| this.setId(0L)
	 */
	public void clearId() {
		this.setId(0L);
	}

	/**
	 * This method returns whether a school can be used or not
	 * 
	 * @param school
	 * 		This parameter is used as school
	 * 
	 * @return ...
	 * 		| school == null || !school.isTerminated()
	 * 
	 */
	@Basic
	public boolean canHaveAsSchool(School school) {
		return school == null || !school.isTerminated();
	}
	
	/**
	 * This method returns whether this slime is attached to a school
	 * 
	 * @return ...
	 * 		| result == canHaveAsSchool(getSchool()) && getSchool() != null &&  getSchool().getSlimes().contains(this)
	 */
	public boolean hasProperSchool() {
		return canHaveAsSchool(getSchool()) && (getSchool() == null ||  getSchool().hasProperSlime(this));
	}

	/**
	 * This method returns the school
	 * 
	 * @return ...
	 * 		|result == school
	 * 
	 */
	@Basic
	public School getSchool() {
		return this.school;
	}
	
	/**
	 * This method is used to set the school
	 * 
	 * @param school
	 * 		This parameter is used as the school
	 * 
	 * @throws IllegalArgumentException
	 * 		...
	 * 		| !canHaveAsSchool(school)
	 * @post ...
	 * 		|(new this).school = school
	 * 
	 */
	public void setSchool(School school){
		if(!canHaveAsSchool(school)) throw new IllegalArgumentException();
		this.school = school;
	}
	
	/**
	 * This method return whether the new school can be used to place the slime
	 * 
	 * @param school
	 * 			This parameter is used as school
	 * 
	 * @return ...
	 * 			|result == (getSchool() != null && !isTerminated() && !school.isTerminated())
	 */
	public boolean canHaveAsNewSchool(School school) {
		return (getSchool() != null && !isTerminated() && !school.isTerminated());
	}
	
	/**
	 * This method is used to arrange the switch of a slime to a new school
	 * 
	 * @param school
	 * 			This parameter is used as new school
	 * 
	 * @throws IllegalArgumentException 
	 * 		 ...
	 * 		|!canHaveAsNewSchool(school)
	 * @post ...
	 * 		| for(Slime oldSchoolSlime: this.getSchool().getSlimes()) 
	 *		|	if(oldSchoolSlime != this) then 
	 *		|		this.getHitPoints().setPoint(-1) && oldSchoolSlime.getHitPoints().setPoint(+1)
	 * @post ...
	 *		|if(getWorld() != null) then getWorld().transfer(this, this.getSchool(), school)
	 * @post ...
	 *		|for(Slime newSchoolSlime: this.getSchool().getSchool()) 
	 *		|	if(newSchoolSlime != this) then
	 *		|		newSchoolSlime.getHitPoints().setPoint(-1) && this.getHitPoints().setPoint(+1)
	 *
	 */
	@Raw
	public void setNewSchool(School school) {
		if(!canHaveAsNewSchool(school)) throw new IllegalArgumentException("You can not use this school as new school for this slime");
		Transferrator transferrator = nextSchool -> {
			for(Slime oldSchoolSlime: Slime.this.getSchool().getSlimes()) {
				if(oldSchoolSlime != Slime.this) {
					Slime.this.updateHitPoints(-1);
					oldSchoolSlime.updateHitPoints(1);
				}
			}
			Slime.this.getSchool().removeSlime(Slime.this);
			nextSchool.addSlime(Slime.this);
			Slime.this.setSchool(nextSchool);
			for(Slime newSchoolSlime: Slime.this.getSchool().getSlimes()) {
				if(newSchoolSlime != Slime.this) {
				newSchoolSlime.updateHitPoints(-1);
				Slime.this.updateHitPoints(1);
				}
			}
		};
		transferrator.transfer(school);
	}
	
	/**
	 * This method is used to synchronize the school
	 * 
	 * @post...
	 * 		| if(getSchool() == null) then return
	 *		| getSchool().getSlimes().stream().filter(slime -> slime != this).forEach(slime -> slime.setHit(-1))
	 *
	 */
	public void synchronizeSchool() {
		if(getSchool() == null) return;
		getSchool().getSlimes().stream().filter(slime -> slime != this).forEach(slime -> slime.updateHitPoints(-1));
	}

	@Override
	public boolean canMoveRight() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getRightBorder()).isEmpty() && super.getWorld().shallBePassable(getRightBorder());
	}

	@Override
	public boolean isMovingRight() {
		return ((RunKinematics) getKinematics()).getVelocity() > 0 || ((RunKinematics) getKinematics()).getAcceleration() > 0;
	}

	@Override
	public boolean canMoveLeft() {
		if(isTerminated() || isDead()) return false;
		if(isStillNotInAGameWorld()) return true;
		return super.getCollidingCreatures(getLeftBorder()).isEmpty() && super.getWorld().shallBePassable(getLeftBorder());
	}

	@Override
	public boolean isMovingLeft() {
		return ((RunKinematics) getKinematics()).getVelocity() < 0 || ((RunKinematics) getKinematics()).getAcceleration() < 0;
	}

	@Override
	public void startMoveRight() {
		assert(!isMovingRight() && !isDead());
		((RunKinematics) getKinematics()).setAcceleration(0.7);
		((RunKinematics) getKinematics()).setVelocity(0.0);
		((RunKinematics) getKinematics()).setVelocityBounds(0.0,2.5);
	}

	@Override
	public void endMove() {
		assert(isMovingHorizontally() && !isDead());
		((RunKinematics) getKinematics()).setVelocity(0.0);
		((RunKinematics) getKinematics()).setAcceleration(0.0);
	}

	@Override
	public void startMoveLeft() {
		assert(!isMovingLeft() && !isDead());
		((RunKinematics) getKinematics()).setAcceleration(-0.7);
		((RunKinematics) getKinematics()).setVelocity(0.0);
		((RunKinematics) getKinematics()).setVelocityBounds(0.0,-2.5);
	}

	@Override
	protected Collidable initializeCollider() {
		return new SlimeCollider(this);
	}

	@Override
	protected FeatureHandler initializeFeatureHandler() {
		return new SlimeFeatureHandler(this);
	}

	@Override
	protected Kinematics initializeKinematics() {
		return new RunKinematics();
	}

	@Override
	protected GameObjectVisualizer initializeVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites) {
		return new SlimeVisualizer(this, xCoordinate, yCoordinate, sprites);
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
		if(getWorld() != null && getWorld().getPlayer() != null) {
			if(getWorld().getPlayer().getBlockTime() != 0 && !getWorld().getPlayer().isAdvancedByWorld()){
				getWorld().getPlayer().setBlockTime(getWorld().getPlayer().getBlockTime() + getTimeStep());
			}
		}
		if(!isMovingHorizontally()) {
			if(((RunKinematics) getKinematics()).getMaxVelocity() > 0 && canMoveRight()) {
				startMoveRight();
			}else if(((RunKinematics) getKinematics()).getMinVelocity() < 0 && canMoveLeft()){
				startMoveLeft();
			}
		}else{
			updateHorizontalMovement();
		}
		getKinematics().updatePosition(getTimeStep(), this);
		if(!isInside()) terminate();
		super.getFeatureHandler().handleFeature();
		super.updateImage();
		if(isDead()){
			setDelay(getDelay() + getTimeStep());
			if(getDelay() == Constant.REMOVE_DELAY.getValue()) terminate();
		}
	}

	@Override
	protected void accept(Collidable collidable) {
		collidable.collideWithSlime(this);
	}

	@Override
	public void addToStorage(GameObjectStorage worldStorage) {
		worldStorage.addSlime(this);
	}

	@Override
	public void removeFromStorage(GameObjectStorage worldStorage) {
		worldStorage.removeSlime(this);
	}

	public void terminate(){
		if(this.getSchool() != null) {
			this.getSchool().removeSlime(this);
		}
		super.terminate();
	}

}
