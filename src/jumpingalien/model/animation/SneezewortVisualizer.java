package jumpingalien.model.animation;

import jumpingalien.model.Sneezewort;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.util.Sprite;

public class SneezewortVisualizer extends GameObjectVisualizer {

    private Sneezewort sneezewort;

    public SneezewortVisualizer(Sneezewort sneezewort, int xCoordinate, int yCoordinate, Sprite[] sprites) {
        super(xCoordinate, yCoordinate, sprites);
        setSneezewort(sneezewort);
    }

    public Sneezewort getSneezewort() {
        return sneezewort;
    }

    public void setSneezewort(Sneezewort sneezewort) {
        this.sneezewort = sneezewort;
    }

    @Override
    public void updateImage() {
        if(sneezewort.isMovingLeft()) setImage(0);
        if(sneezewort.isMovingRight()) setImage(1);
    }
}
