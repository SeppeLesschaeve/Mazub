package jumpingalien.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;

/**
 * This class holds a couple of rectangles with the origin at the bottom-left that tells 
 * where the Game Object is located (the whole rectangle and its left-, right-, up- and down-border), 
 * images to show the animation of the Game Object and one of them is used to display the current pose;
 * a world where the Game Object can be attached to that will handle the collision with other Game Objects
 * 
 * @invar ...
 * 		| hasProperWorld()
 * 
 * @version 3.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public abstract class GameObject{
	
	private Rectangle rectangle;
	private Rectangle upBorder;
	private Rectangle downBorder;
	private Rectangle leftBorder;
	private Rectangle rightBorder;
	private Position<Double> position;
	private Velocity velocity = new Velocity(0.0, 0.0);
	private Sprite currentImage;
	private Sprite[] images;
	private World world;
	private double blockTime = 0.0;
	private double delay = 0.0;
	protected static final double REMOVE_DELAY = 0.6;

	@Model
	protected GameObject(int x, int y, Sprite... sprites) throws IllegalArgumentException{
		if(x < 0 || y < 0) throw new IllegalArgumentException("You can not create the alien outside the universe");
		for(Sprite sprite: sprites)
			if(sprite == null) throw new IllegalArgumentException();
		this.images = sprites.clone();
		this.currentImage = images[0];
		this.rectangle = new Rectangle(x, y, currentImage.getWidth(), currentImage.getHeight());
		this.position = new Position<>(x*0.01, y*0.01);
		setBorders();
	}
	
	protected void setBorders() {
		int x = rectangle.getOrigin().getX();
		int y = rectangle.getOrigin().getY();
		setUpBorder(new Rectangle(x, y + currentImage.getHeight() - 1, currentImage.getWidth(), 1));
		setDownBorder(new Rectangle(x, y, currentImage.getWidth(), 1));
		setLeftBorder(new Rectangle(x, y, 1, currentImage.getHeight()));
		setRightBorder(new Rectangle(x + currentImage.getWidth() - 1, y, 1, currentImage.getHeight()));
	}
	
	public boolean hasProperWorld() {
		return canHaveAsWorld(getWorld()) && (getWorld() ==  null || getWorld().hasProperGameObject(this));
	}
	
	public boolean canHaveAsWorld(World world) {
		if(world != null) return getWorld() == world;
		return !isTerminated();
	}
	
	@Basic
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(rectangle.getOrigin(), rectangle.getDimension());
	}
	
	public Rectangle getUp() {
		return upBorder;
	}

	protected void setUpBorder(Rectangle upBorder) {
		this.upBorder = upBorder;
	}

	public Rectangle getDown() {
		return downBorder;
	}

	protected void setDownBorder(Rectangle downBorder) {
		this.downBorder = downBorder;
	}

	public Rectangle getLeft() {
		return leftBorder;
	}

	protected void setLeftBorder(Rectangle leftBorder) {
		this.leftBorder = leftBorder;
	}

	public Rectangle getRight() {
		return rightBorder;
	}

	protected void setRightBorder(Rectangle rightBorder) {
		this.rightBorder = rightBorder;
	}

	@Basic
	public Dimension getDimension() {
		return rectangle.getDimension();
	}
	
	@Basic
	public Position<Integer> getOrigin() {
		return rectangle.getOrigin();
	}
	
	@Basic
	public Position<Double> getPosition() {
		return position;
	}
	
	@Basic
	public Velocity getVelocity() {
		return velocity;
	}

	@Basic
	public Sprite[] getSprites() {
		return this.images.clone();
	}

	public int getIndex() {
		return Arrays.asList(getSprites()).indexOf(getSprite());
	}

	@Basic
	public Sprite getSprite() {
		return currentImage;
	}
	
	protected void setSprite(Sprite sprite) {
		this.currentImage = sprite;
	}

	protected void setSprite(int index){
		if(index < 8) {
			setSprite(getSprites()[index]);
		}else if(getOrientation() == Orientation.POSITIVE.getValue() && index >= ((images.length-8)/2)+8){
			index = 8;
		}else if(getOrientation() == Orientation.NEGATIVE.getValue() && index >= images.length) {
			index = ((images.length-8)/2)+8;
		}
		setSprite(getSprites()[index]);
		this.getRectangle().setDimension(currentImage.getWidth(), currentImage.getHeight());
	}

		
	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay += delay;
		if(this.delay > REMOVE_DELAY) this.delay = REMOVE_DELAY;
	}
	
	public double getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(double blockTime) {
		this.blockTime = blockTime;
	}

	public boolean isRunning() {
		return this.getVelocity().getX() != 0;
	}

	public abstract int getOrientation();
	public abstract Acceleration getAcceleration();
	public abstract void advanceTime(double deltaT);
	public abstract boolean isDead();
	protected abstract double updateDt(double deltaT, double time);
	
	public void terminate() {
		if(this instanceof Slime && ((Slime) this).getSchool() != null) {
				((Slime) this).getSchool().removeSlime(((Slime) this));
		}
		if(this instanceof Creature && ((Creature)this).getHitPoints() != 0) 
			((Creature) this).setHitPoints(-((Creature)this).getHitPoints());
		if(this instanceof Plant && ((Plant)this).getHit() != 0) 
			((Plant) this).getHitPoint().setPoints(-((Plant)this).getHit());
		if(this instanceof Spider) ((Spider) this).setLegs(0);
		if(getWorld() == null) return;
		if(this == getWorld().getPlayer()) this.getWorld().unsetPlayer();
		this.getWorld().removeGameObject(this);
		this.setWorld(null);
	}

	public boolean isTerminated() {
		return (this.getWorld() == null && isDead());
	}

	protected Rectangle getUpBorder() {
		Rectangle up = null;
		if(this instanceof Spider) {
			return new Rectangle(getUp().getOrigin().getX(), getUp().getOrigin().getY() + 1, getUp().getDimension());
		}
		if(getOrientation() == Orientation.NEGATIVE.getValue()) {
			up = new Rectangle(getUp().getOrigin().getX()-1, getUp().getOrigin().getY() + 1, getUp().getDimension());
		}else if(getOrientation() == Orientation.NEUTRAL.getValue()) {
			up = new Rectangle(getUp().getOrigin().getX(), getUp().getOrigin().getY() + 1, getUp().getDimension());
		}else if(getOrientation() == Orientation.POSITIVE.getValue()) {
			up = new Rectangle(getUp().getOrigin().getX() + 1, getUp().getOrigin().getY() + 1, getUp().getDimension());
		}
		return up;
	}
	
	protected Rectangle getDownBorder() {
		Rectangle down = null;
		if(this instanceof Spider) {
			return new Rectangle(getDown().getOrigin().getX(), getDown().getOrigin().getY() - 1, getDown().getDimension());
		}
		if(getOrientation() == Orientation.NEGATIVE.getValue() ) {
			down = new Rectangle(getDown().getOrigin().getX() - 1, getDown().getOrigin().getY() - 1, getDown().getDimension());
		}else if(getOrientation() == Orientation.NEUTRAL.getValue()) {
			down = new Rectangle(getDown().getOrigin().getX(), getDown().getOrigin().getY() - 1, getDown().getDimension());
		}else if(getOrientation() == Orientation.POSITIVE.getValue()) {
			down = new Rectangle(getDown().getOrigin().getX() + 1, getDown().getOrigin().getY() - 1, getDown().getDimension());
		}
		return down;
	}
	
	protected Rectangle getLeftBorder() {
		Rectangle left = null;
		if(this instanceof Spider) {
			return new Rectangle(getLeft().getOrigin().getX() - 1 , getLeft().getOrigin().getY(), getLeft().getDimension());
		}
		if(getOrientation() == Orientation.NEGATIVE.getValue() && getVelocity().getY() > 0) {
			left = new Rectangle(getLeft().getOrigin().getX() -1, getLeft().getOrigin().getY() + 2, getLeft().getDimension());
		}else if(getOrientation() == Orientation.NEGATIVE.getValue() && getVelocity().getY() == 0) {
			left = new Rectangle(getLeft().getOrigin().getX()-1, getLeft().getOrigin().getY() + 1, getLeft().getDimension());
		}else if(getOrientation() == Orientation.NEGATIVE.getValue() && getVelocity().getY() < 0) {
			left = new Rectangle(getLeft().getOrigin().getX()-1, getLeft().getOrigin().getY(), getLeft().getDimension());
		}
		return left;
	}

	protected Rectangle getRightBorder() {
		Rectangle right = null;
		if(this instanceof Spider) {
			return new Rectangle(getRight().getOrigin().getX() + 1 , getRight().getOrigin().getY(), getRight().getDimension());
		}
		if(getOrientation() == Orientation.POSITIVE.getValue() && getVelocity().getY() > 0) {
			right = new Rectangle(getRight().getOrigin().getX() + 1, getRight().getOrigin().getY() + 2, getRight().getDimension());
		}else if(getOrientation() == Orientation.POSITIVE.getValue() && getVelocity().getY() == 0) {
			right = new Rectangle(getRight().getOrigin().getX() + 1, getRight().getOrigin().getY() + 1, getRight().getDimension());
		}else if(getOrientation() == Orientation.POSITIVE.getValue() && getVelocity().getY() < 0) {
			right = new Rectangle(getRight().getOrigin().getX() + 1, getRight().getOrigin().getY(), getRight().getDimension());
		}
		return right;
	}
	
	public boolean isInside() {
		if(this.getWorld() == null) {return true;}
		if(this.getPosition().getX() < 0 || this.getPosition().getY() < 0) return false;
		return this.getWorld().getGameWorld().contains(getOrigin());
	}
	
	public void changeActualPosition(double[] newPosition) throws IllegalArgumentException{
		if(newPosition == null || newPosition.length != 2 || (((Double) newPosition[0]).isNaN() || ((Double) newPosition[1]).isNaN()))
			throw new IllegalArgumentException("The desired position can not be null, must be two-dimensional and not NaN values.");
		if(getWorld() != null && !(this instanceof Plant)) {
			for(int newX = (int)(newPosition[0]/0.01), pixelX = newX; pixelX < newX+currentImage.getWidth(); pixelX+=getWorld().getTileLength()) {
				for(int newY = (int)(newPosition[1]/0.01), pixelY= newY; pixelY < newY+currentImage.getHeight(); pixelY+= getWorld().getTileLength()) {
					if(!getWorld().getTileFeature(pixelX, pixelY).isPassable()) 
						throw new IllegalArgumentException("You can not place the Game Object in Solid Ground");
				}
			}
		}
		getPosition().setPosition(newPosition[0], newPosition[1]);
		getRectangle().setOrigin((int)(newPosition[0]/0.01), (int)(newPosition[1]/0.01));
		if(getWorld() != null && !getWorld().getGameWorld().contains(getOrigin())) terminate();
	}
	
	protected Set<GameObject> overlappingGameObject(Rectangle rect){
		Set<GameObject> overlappingObjects = new HashSet<>();
		if(this.getWorld() == null || this.isTerminated() || rect == null) return overlappingObjects;
		for(Object object: this.getWorld().getGameObjects()) {
			if(!(object instanceof Plant) || this instanceof Mazub) {
				if(object instanceof Plant) {
					overlappingObjects.add((Plant) object);
				}else if(rect.overlaps(((GameObject) object).getRectangle()) && object != this) {
					overlappingObjects.add((GameObject) object);
				}
			}
		}
		return overlappingObjects;
	}
	
	protected Set<GameObject> getCollidingObjects() {
		Set<GameObject> objects;
		if(getVelocity().getY() > 0) {
			objects = overlappingGameObject(getUpBorder());
			if(objects.isEmpty()) {
				if(getOrientation() ==  Orientation.NEGATIVE.getValue()) objects = overlappingGameObject(getLeftBorder());
				else if(getOrientation() == Orientation.POSITIVE.getValue()) objects = overlappingGameObject(getRightBorder());
			}
		}else {
			objects = overlappingGameObject(getDownBorder());
			if(objects.isEmpty()) {
				if(getOrientation() ==  Orientation.NEGATIVE.getValue()) objects = overlappingGameObject(getLeftBorder());
				else if(getOrientation() == Orientation.POSITIVE.getValue()) objects = overlappingGameObject(getRightBorder());
			}
		}
		return objects;
	}
	
}
