package jumpingalien.model.feature;

import jumpingalien.model.Sneezewort;
import jumpingalien.model.feature.FeatureHandler;

public class SneezewortFeatureHandler implements FeatureHandler {

    private Sneezewort sneezewort;

    public SneezewortFeatureHandler(Sneezewort sneezewort) {
        this.setSneezewort(sneezewort);
    }

    public Sneezewort getSneezewort() {
        return sneezewort;
    }

    public void setSneezewort(Sneezewort sneezewort) {
        this.sneezewort = sneezewort;
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

