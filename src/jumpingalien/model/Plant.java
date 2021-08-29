package jumpingalien.model;

import annotate.Basic;
import jumpingalien.util.Sprite;

/**
 * This class builds a Plant with Hit Points as health and with an age
 * 
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public abstract class Plant extends Organism{
	
	private double age  = 0.0;
	private double moveTime = 0.0;
	private double eatTime = 0.0;
	
	/**
	 * This constructor will set the initial Pixel Position, Actual Position, HitPoint and the images to show the animation
	 * 
	 * @param pixelLeftX
	 * 			This parameter is used as the x-coordinate of the Pixel Position
	 * @param pixelBottomY
	 * 			This parameter is used as the y-coordinate of the Pixel Position
	 * @param sprites
	 * 			This parameter is used as images to show the animation
	 * 
	 * @effect ...
	 * 		| super(pixelLeftX, pixelBottomY, sprites)
	 * @post ...
	 * 		| this.hitPoint = new HitPoint(point, 0, point)
	 */
	protected Plant(int pixelLeftX, int pixelBottomY, int point, Sprite... sprites) {
		super(pixelLeftX, pixelBottomY, point, 0, point, sprites); 
	}

	/**
	 * This method returns Age
	 * 
	 * @return ...
	 * 		|result == age
	 * 
	 */
	@Basic
	public double getAge() {
		return this.age;
	}

	/**
	 * This method is used to set the Age
	 * 
	 * @param age
	 * 			This parameter is used to set the Age
	 * 
	 * @post ...
	 * 		|(new this).age = age
	 * 
	 */
	protected void setAge(double age) {
		this.age = age;
	}
	
	/**
	 * This method returns Timer
	 * 
	 * @return ...
	 * 		|result == timer
	 * 
	 */
	@Basic
	public double getMoveTime() {
		return moveTime;
	}

	/**
	 * This method is used to set the Timer
	 * 
	 * @param time
	 * 			This parameter is used to set the Timer
	 * 
	 * @post ...
	 * 		|(new this).timer = timer
	 * 
	 */
	protected void setMoveTime(double time) {
		this.moveTime = time;
		if(moveTime >= Constant.PLANT_SWITCH_TIME.getValue()) this.moveTime = 0.0;
	}

	public double getEatTime() {
		return eatTime;
	}

	public void setEatTime(double eatTime) {
		this.eatTime = eatTime;
		if(this.eatTime >= Constant.TIMEOUT.getValue()) this.eatTime = 0.0;
	}

	public void move(double time){
		setTimeStep(time);
		if(!isDead())getKinematics().updatePosition(getTimeStep(),this);
		if(!isInside()) terminate();
		arrangeEat();
		setMoveTime(getMoveTime() + time);
		updateImage();
	}

	protected void arrangeEat() {
		Rectangle fullRectangle = new Rectangle(getXCoordinate(), getYCoordinate(), getImageWidth(), getImageHeight());
		if(getWorld() != null && getCollidingCreatures(fullRectangle).contains(getWorld().getPlayer())){
			if(getEatTime() != 0.0 && !getWorld().getPlayer().isAdvancedByWorld()) {
				setEatTime(getEatTime() + getTimeStep());
			}
			getWorld().getPlayer().accept(getCollider());
		}
		if(getWorld() != null && !getCollidingCreatures(fullRectangle).contains(getWorld().getPlayer())){
			setEatTime(0.0);
		}
	}
}
