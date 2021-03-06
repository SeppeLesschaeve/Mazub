package jumpingalien.model.feature;

import annotate.Basic;

public enum Feature {
	
	AIR(0), SOLID_GROUND(1), WATER(2), MAGMA(3), ICE(4), GAS(5);
	private int featureId;
	
	Feature(int featureId) {
		this.featureId = featureId;
	}

	@Basic
	public int getFeatureId() {
		return featureId;
	}

	public static Feature getFeature(int featureId) {
		for(Feature feature: Feature.values()) {
			if (feature.getFeatureId() == featureId) return feature;
		}
		return AIR;
	}

	public boolean isPassable() { 
		return featureId != SOLID_GROUND.getFeatureId() && featureId != ICE.getFeatureId(); 	
	}

	@Override
	public String toString() {
		return "[Feature:" + getFeatureId() + "]";
	}

}
