package jumpingalien.model;

public interface FeatureHandler {
	
	public <T extends Organism> void handleFeatureHit(T object, double time);

}
