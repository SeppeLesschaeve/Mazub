package jumpingalien.model.animation;

import jumpingalien.model.Skullcab;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.util.Sprite;

public class SkullcabVisualizer extends GameObjectVisualizer {

    private Skullcab skullcab;

    public SkullcabVisualizer(Skullcab skullcab, int xCoordinate, int yCoordinate, Sprite[] sprites) {
        super(xCoordinate, yCoordinate, sprites);
        this.setSkullcab(skullcab);
    }

    public Skullcab getSkullcab() {
        return skullcab;
    }

    public void setSkullcab(Skullcab skullcab) {
        this.skullcab = skullcab;
    }

    @Override
    public void updateImage() {
        if(skullcab.isJumping()) setImage(0);
        if(skullcab.isFalling()) setImage(1);
    }
}
