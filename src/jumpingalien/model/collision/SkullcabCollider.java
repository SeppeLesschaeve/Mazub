package jumpingalien.model.collision;

import jumpingalien.model.*;

public class SkullcabCollider implements Collidable {

    private Skullcab skullcab;

    public SkullcabCollider(Skullcab skullcab) {
        setSkullcab(skullcab);
    }

    public Skullcab getSkullcab() {
        return skullcab;
    }

    public void setSkullcab(Skullcab skullcab) {
        this.skullcab = skullcab;
    }

    @Override
    public void collideWithMazub(Mazub mazub) {
        if(skullcab.isDead()){
            mazub.updateHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
            skullcab.terminate();
        }else if(skullcab.getEatTime() == 0 && mazub.getHitPoints() < mazub.getMaxHitPoints()){
            mazub.updateHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
            skullcab.updateHitPoints(-1);
            skullcab.setEatTime(skullcab.getEatTime() + skullcab.getTimeStep());
        }
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
