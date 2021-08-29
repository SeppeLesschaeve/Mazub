package jumpingalien.model.collision;

import jumpingalien.model.*;
import jumpingalien.model.collision.Collidable;

public class SneezewortCollider implements Collidable {

    private Sneezewort sneezewort;

    public SneezewortCollider(Sneezewort sneezewort) {
        this.setSneezewort(sneezewort);
    }

    public Sneezewort getSneezewort() {
        return sneezewort;
    }

    public void setSneezewort(Sneezewort sneezewort) {
        this.sneezewort = sneezewort;
    }


    @Override
    public void collideWithMazub(Mazub mazub) {
        sneezewort.updateHitPoints(-1);
        sneezewort.terminate();
    }

    @Override
    public void collideWithShark(Shark shark) {

    }

    @Override
    public void collideWithSkullcab(Skullcab skullcab) {

    }

    @Override
    public void collideWithSlime(Slime slime) {

    }

    @Override
    public void collideWithSneezewort(Sneezewort sneezewort) {

    }

    @Override
    public void collideWithSpider(Spider spider) {

    }
}
