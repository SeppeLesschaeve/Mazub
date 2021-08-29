package jumpingalien.model.animation;

import jumpingalien.model.Slime;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.util.Sprite;

public class SlimeVisualizer extends GameObjectVisualizer {

    private Slime slime;

    public SlimeVisualizer(Slime slime, int xCoordinate, int yCoordinate, Sprite[] sprites) {
        super(xCoordinate, yCoordinate, sprites);
        setSlime(slime);
    }

    public Slime getSlime() {
        return slime;
    }

    public void setSlime(Slime slime) {
        this.slime = slime;
    }

    @Override
    public void updateImage() {
        if(slime.isMovingRight()) setImage(0);
        if(slime.isMovingLeft()) setImage(1);
    }
}
