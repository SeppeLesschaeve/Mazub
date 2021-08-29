package jumpingalien.model.feature;

import jumpingalien.model.Constant;
import jumpingalien.model.Slime;

public class SlimeFeatureHandler implements FeatureHandler {

    private Slime slime;
    private double gasTime = 0.0;
    private double waterTime = 0.0;


    public SlimeFeatureHandler(Slime slime) {
        this.setSlime(slime);
    }

    public Slime getSlime() {
        return slime;
    }

    public void setSlime(Slime slime) {
        this.slime = slime;
    }

    public double getGasTime() {
        return gasTime;
    }

    public void setGasTime(double gasTime) {
        this.gasTime = gasTime;
    }

    public double getWaterTime() {
        return waterTime;
    }

    public void setWaterTime(double waterTime) {
        this.waterTime = waterTime;
    }

    public Boolean[] getFeatureScore() {
        Boolean[] features = new Boolean[] {false, false, false};
        if(slime.getWorld() == null) return features;
        int tileLength = slime.getWorld().getTileLength();
        for(int newX, pixelX = slime.getXCoordinate(); pixelX <= slime.getXCoordinate() + slime.getImageWidth() - 1; pixelX += newX) {
            for(int newY, pixelY = slime.getYCoordinate(); pixelY <= slime.getYCoordinate() + slime.getImageHeight() - 1; pixelY += newY) {
                if(slime.getWorld().getTileFeature(pixelX, pixelY) == Feature.MAGMA) features[0] = true;
                if(slime.getWorld().getTileFeature(pixelX, pixelY) == Feature.GAS)   features[1] = true;
                if(slime.getWorld().getTileFeature(pixelX, pixelY) == Feature.WATER) features[2] = true;
                newY = Math.min(tileLength, slime.getYCoordinate() + slime.getImageHeight() - 1 - pixelY);
                if(newY < tileLength) newY = 1;
            }
            newX = Math.min(tileLength, slime.getXCoordinate() + slime.getImageWidth() - 1 - pixelX);
            if(newX < tileLength) newX = 1;
        }
        return features;
    }

    @Override
    public void handleFeature() {
        if(slime.getHitPoints() == 0) return;
        Boolean[] features =  getFeatureScore();
        if(Boolean.TRUE.equals(features[0])) handleMagmaHit();
        if(Boolean.TRUE.equals(features[1])) {
            handleGasHit();
        }else{
            setGasTime(0.0);
        }
        if(Boolean.TRUE.equals(features[2])) {
            handleWaterHit();
        }else{
            setWaterTime(0.0);
        }
    }

    @Override
    public void handleGasHit() {
        setGasTime(gasTime + slime.getTimeStep());
        if(gasTime >= Constant.SLIME_GAS_TIME.getValue()) {
            slime.updateHitPoints((int) Constant.SLIME_GAS.getValue());
            slime.synchronizeSchool();
            setGasTime(0.0);
        }
    }

    @Override
    public void handleIceHit() {

    }

    @Override
    public void handleMagmaHit() {
        slime.updateHitPoints(-slime.getHitPoints());
        slime.synchronizeSchool();
    }

    @Override
    public void handleWaterHit() {
        setWaterTime(waterTime + slime.getTimeStep());
        if(waterTime >= Constant.SLIME_WATER_TIME.getValue()) {
            slime.updateHitPoints((int) Constant.SLIME_WATER.getValue());
            slime.synchronizeSchool();
            setWaterTime(0.0);
        }
    }
}
