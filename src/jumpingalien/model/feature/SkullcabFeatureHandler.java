package jumpingalien.model.feature;

import jumpingalien.model.Skullcab;
import jumpingalien.model.feature.FeatureHandler;

public class SkullcabFeatureHandler implements FeatureHandler {

    private Skullcab skullcab;

    public SkullcabFeatureHandler(Skullcab skullcab) {
        this.setSkullcab(skullcab);
    }

    public Skullcab getSkullcab() {
        return skullcab;
    }

    public void setSkullcab(Skullcab skullcab) {
        this.skullcab = skullcab;
    }

    @Override
    public void handleFeature() {

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
