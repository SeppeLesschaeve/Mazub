package jumpingalien.model.feature;

import jumpingalien.model.Constant;
import jumpingalien.model.Rectangle;
import jumpingalien.model.Shark;
import jumpingalien.model.feature.FeatureHandler;

public class SharkFeatureHandler implements FeatureHandler {

    private Shark shark;
    private double featureTime = 0.0;

    public SharkFeatureHandler(Shark shark) {
        this.setShark(shark);
    }

    public Shark getShark() {
        return shark;
    }

    public void setShark(Shark shark) {
        this.shark = shark;
    }

    public double getFeatureTime() {
        return featureTime;
    }

    public void setFeatureTime(double featureTime) {
        this.featureTime = featureTime;
    }

    @Override
    public void handleFeature() {
        if(!shark.isInWater(new Rectangle(shark.getXCoordinate(), shark.getYCoordinate(),
                shark.getImageWidth(), shark.getImageHeight()))) {
            setFeatureTime(featureTime + shark.getTimeStep());
            if(featureTime >= Constant.SHARK_OUT_WATER_TIME.getValue()) {
                shark.updateHitPoints((int) Constant.SHARK_OUT_WATER.getValue());
                setFeatureTime(0.0);
            }
        }else {
            setFeatureTime(0.0);
        }
    }

    @Override
    public void handleGasHit() {

    }

    @Override
    public void handleIceHit() {

    }

    @Override
    public void handleMagmaHit() {

    }

    @Override
    public void handleWaterHit() {

    }
}

