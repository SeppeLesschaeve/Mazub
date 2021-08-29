package jumpingalien.model;

import jumpingalien.model.hit.HitPoint;
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
public abstract class Organism extends GameObject {
	
	private HitPoint hitPoint;

	protected Organism(int x, int y, int hitPoints, int minimum, int maximum, Sprite... sprites) throws IllegalArgumentException{
		super(x,y, sprites);
		this.hitPoint = new HitPoint(hitPoints, minimum, maximum);
	}

	public int getHitPoints() {
		return hitPoint.getHitPoints();
	}
	
	public void updateHitPoints(int hitPoints) {
		this.hitPoint.updateHitPoints(hitPoints);
	}

	public int getMaxHitPoints() {
		return hitPoint.getMaximum();
	}
	
	public void terminate() {
		hitPoint.setHitPoints(0);
		super.terminate();
	}


}
