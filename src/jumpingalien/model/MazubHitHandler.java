package jumpingalien.model;

public abstract class MazubHitHandler {
	
	public abstract void arrangeSlimeHit(double dt);
	
	public abstract void arrangeSneezeHit(Sneezewort sneezewort, double dt);
	
	public abstract void arrangeSkullHit(Skullcab skullcab, double dt);
	
	public abstract void arrangeSharkHit(double dt);
	
	public abstract void arrangeSpiderHit();

}
