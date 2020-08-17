package jumpingalien.model;

public class MazubFeatureHandler implements FeatureHandler{
	
	private double featureTime;
	private Feature previousFeature;
	private Mazub mazub;

	public MazubFeatureHandler(Mazub mazub) {
		this.mazub = mazub;
		this.previousFeature = mazub.getDominantFeature();
	}

	@Override
	public <T extends Organism> void handleFeatureHit(T object, double time) {
		if(mazub.getHitPoints() == 0) return;
		Feature feature = mazub.getDominantFeature();
		if(feature != previousFeature) {
			featureTime = 0;
		}
		if(feature == Feature.MAGMA) handleMagmaHit(time);
		if(feature == Feature.GAS) handleGasHit(time);
		if(feature == Feature.WATER) handleWaterHit(time);
		this.previousFeature = feature;
		if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()) featureTime = 0;
	}

	public void handleMagmaHit(double time) {
		if(featureTime == 0.0 && previousFeature != Feature.MAGMA) {
			mazub.setHitPoints((int) Constant.MAZUB_MAGMA.getValue());
			featureTime += time;
		}else {
			featureTime += time; 
			if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()) mazub.setHitPoints((int) Constant.MAZUB_MAGMA.getValue());
		}
	}

	public void handleGasHit(double time) {
		if(featureTime == 0.0 && previousFeature != Feature.GAS) {
			mazub.setHitPoints((int) Constant.MAZUB_GAS.getValue());
			featureTime += time;
		}else {
			featureTime += time; 
			if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()) mazub.setHitPoints((int) Constant.MAZUB_GAS.getValue());
		}
	}

	public void handleWaterHit(double time) {
		featureTime += time; 
		if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()) mazub.setHitPoints((int) Constant.MAZUB_WATER.getValue());
	}

}
