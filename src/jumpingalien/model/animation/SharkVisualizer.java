package jumpingalien.model.animation;

import jumpingalien.model.Shark;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.util.Sprite;

public class SharkVisualizer extends GameObjectVisualizer {

    private Shark shark;

    public SharkVisualizer(Shark shark, int xCoordinate, int yCoordinate, Sprite[] sprites) {
        super(xCoordinate, yCoordinate, sprites);
        this.setShark(shark);
    }

    public Shark getShark() {
        return shark;
    }

    public void setShark(Shark shark) {
        this.shark = shark;
    }


    @Override
    public void updateImage() {
        setImage(shark.getSharkStateID());
    }
}
