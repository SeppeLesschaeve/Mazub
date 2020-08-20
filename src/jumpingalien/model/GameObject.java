package jumpingalien.model;

import java.util.Arrays;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

public abstract class GameObject {
	
	private Rectangle rectangle;
	private Position<Double> position;
	private Sprite image;
	private Sprite[] animationImages;
	private World world;
	
	public GameObject(int xCoordinate, int yCoordinate, Sprite...sprites) {
		if(xCoordinate < 0 || yCoordinate < 0) throw new IllegalArgumentException("You can not create the alien outside the universe");
		for(Sprite sprite: sprites)
			if(sprite == null) throw new IllegalArgumentException();
		this.animationImages = sprites.clone();
		this.image = animationImages[0];
		this.rectangle = new Rectangle(xCoordinate, yCoordinate, image.getWidth(), image.getHeight());
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
		return rectangle.clone();
	}
	
	public void updateHorizontalComponent(double x) {
		rectangle.updateHorizontalComponent((int)(x/0.01));
		position.setX(x);
	}
	
	public void updateHorizontalComponent(int x) {
		rectangle.updateHorizontalComponent(x);
		position.setX(x*0.01);
	}
	
	public void updateVerticalComponent(double y) {
		rectangle.updateVerticalComponent((int)(y/0.01));
		position.setY(y);
	}
	
	public void updateVerticalComponent(int y) {
		rectangle.updateVerticalComponent(y);
		position.setY(y*0.01);
	}
	
	public void updateDimension(int x, int y) {
		rectangle.setDimension(x, y);
	}
	
	public void updatePosition(int x, int y) {
		updateHorizontalComponent(x);
		updateVerticalComponent(y);
	}
	
	public void updatePosition(double x, double y) {
		updateHorizontalComponent(x);
		updateVerticalComponent(y);
	}
	
	@Basic
	public Position<Double> getPosition() {
		return position.clone();
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
		return image;
	}
	
	protected void setSprite(Sprite sprite) {
		this.image = sprite;
	}
	
	protected abstract void setSprite(int index);

	@Basic
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public boolean isTerminated() {
		return world == null;
	};
}
