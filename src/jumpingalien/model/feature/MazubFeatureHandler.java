package jumpingalien.model.feature;

import jumpingalien.model.Constant;
import jumpingalien.model.Mazub;
import jumpingalien.model.feature.FeatureHandler;

public class MazubFeatureHandler implements FeatureHandler {

    private Mazub mazub;
    private double featureTime = 0.0;
    private Feature previousFeature;

    public MazubFeatureHandler(Mazub mazub) {
        this.setMazub(mazub);
    }

    public Mazub getMazub() {
        return mazub;
    }

    public void setMazub(Mazub mazub) {
        this.mazub = mazub;
    }

    public double getFeatureTime() {
        return featureTime;
    }

    public void setFeatureTime(double featureTime) {
        this.featureTime = featureTime;
    }

    public Feature getPreviousFeature() {
        return previousFeature;
    }

    public void setPreviousFeature(Feature previousFeature) {
        this.previousFeature = previousFeature;
    }

    private Feature getDominantFeature() {
        if(mazub.getWorld() == null) return Feature.AIR;
        Feature feature = Feature.AIR;
        int tileLength = mazub.getWorld().getTileLength();
        for(int newX, pixelX = mazub.getXCoordinate(); pixelX <= mazub.getXCoordinate() + mazub.getImageWidth() - 1; pixelX += newX) {
            for(int newY, pixelY = mazub.getYCoordinate(); pixelY <= mazub.getYCoordinate() + mazub.getImageHeight() - 1; pixelY += newY) {
                if(mazub.getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) return Feature.MAGMA;
                if(mazub.getWorld().getTileFeature(pixelX, pixelY) == Feature.GAS) feature = Feature.GAS;
                if(mazub.getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER && feature != Feature.GAS) feature = Feature.WATER;
                newY = Math.min(tileLength, mazub.getYCoordinate() + mazub.getImageHeight() - 1 - pixelY);
                if(newY < tileLength) newY = 1;
            }
            newX = Math.min(tileLength, mazub.getXCoordinate() + mazub.getImageWidth() - 1 - pixelX);
            if(newX < tileLength) newX = 1;
        }
        return feature;
    }

    @Override
    public void handleFeature() {
        if(mazub.getWorld() == null || mazub.isDead()) return;
        Feature feature = getDominantFeature();
        if(feature != previousFeature) {
            setFeatureTime(0);
            if(feature == Feature.MAGMA) handleMagmaHit();
            if(feature == Feature.GAS) handleGasHit();
        }
        setFeatureTime(featureTime + mazub.getTimeStep());
        if(featureTime >= Constant.MAZUB_FEATURE_TIME.getValue()){
            if(feature == Feature.WATER) {
                handleWaterHit();
            }
            this.featureTime = featureTime - Constant.MAZUB_FEATURE_TIME.getValue();
            if(feature == Feature.MAGMA) handleMagmaHit();
            if(feature == Feature.GAS) handleGasHit();
        }
        setPreviousFeature(feature);

    }

    @Override
    public void handleGasHit() {
        mazub.updateHitPoints((int) Constant.MAZUB_GAS.getValue());
    }

    @Override
    public void handleIceHit() {

    }

    @Override
    public void handleMagmaHit() {
        mazub.updateHitPoints((int) Constant.MAZUB_MAGMA.getValue());
    }

    @Override
    public void handleWaterHit() {
        mazub.updateHitPoints((int) Constant.MAZUB_WATER.getValue());
    }
}

