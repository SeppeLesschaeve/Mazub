package jumpingalien.model;

public interface FeatureHandler {
	
	public <T extends GameObject> void handleFeatureHit(T object, double time);

}
