package jumpingalien.model;

import java.util.Arrays;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

public abstract class GameObject {
	
	private Rectangle rectangle;
	private Position<Double> position;
	private Sprite getSprite;
	private Sprite[] animationImages;
	private World world;
	
	public GameObject(int xCoordinate, int yCoordinate, Sprite...sprites) {
		if(xCoordinate < 0 || yCoordinate < 0) throw new IllegalArgumentException("You can not create the alien outside the universe");
		for(Sprite sprite: sprites)
			if(sprite == null) throw new IllegalArgumentException();
		this.animationImages = sprites.clone();
		this.getSprite = animationImages[0];
		this.rectangle = new Rectangle(xCoordinate, yCoordinate, getSprite.getWidth(), getSprite.getHeight());
		this.position = new Position<>(xCoordinate*0.01, yCoordinate*0.01);
	}
	
	public boolean hasProperWorld() {
		return canHaveAsWorld(getWorld()) && (getWorld() ==  null || getWorld().hasProperGameObject(this));
	}
	
	public boolean canHaveAsWorld(World world) {
		if(world != null) return getWorld() == world;
		return !isTerminated();
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
	
	@Basic
	public Position<Double> getPosition() {
		return position;
	}
	
	@Basic
	public Sprite[] getSprites() {
		return this.animationImages.clone();
	}

	public int getIndex() {
		return Arrays.asList(getSprites()).indexOf(getSprite());
	}

	@Basic
	public Sprite getSprite() {
		return getSprite;
	}
	
	protected void setSprite(Sprite sprite) {
		this.getSprite = sprite;
	}
	
	protected abstract void setSprite(int index);

	@Basic
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	protected abstract boolean isTerminated();
}
