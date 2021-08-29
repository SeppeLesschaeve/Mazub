package jumpingalien.model.collision;

import jumpingalien.model.*;

public class MazubCollider implements Collidable {


    private Mazub mazub;

    public MazubCollider(Mazub mazub){
        setMazub(mazub);
    }

    public Mazub getMazub() {
        return mazub;
    }

    public void setMazub(Mazub mazub) {
        this.mazub = mazub;
    }

    @Override
    public void collideWithMazub(Mazub mazub) {

    }

    @Override
    public void collideWithShark(Shark shark) {
        if(mazub.isDead() || shark.isDead()) return;
        if(mazub.getBlockTime() == 0) {
            mazub.updateHitPoints((int) Constant.MAZUB_SHARK.getValue());
            shark.updateHitPoints((int) Constant.SHARK_MAZUB.getValue());
            mazub.setBlockTime(mazub.getTimeStep());
        }
    }

    @Override
    public void collideWithSkullcab(Skullcab skullcab) {
        if(skullcab.getEatTime() == 0) {
            if(skullcab.isDead()) {
                mazub.updateHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
                skullcab.terminate();
            }else if(mazub.getHitPoints() < mazub.getMaxHitPoints()){
                mazub.updateHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
                skullcab.updateHitPoints(-1);
                if(skullcab.isDead()) skullcab.terminate();
            }
            skullcab.setEatTime(skullcab.getEatTime() + mazub.getTimeStep());
        }
    }

    @Override
    public void collideWithSlime(Slime slime) {
        if(mazub.isDead() || slime.isDead()) return;
        if(mazub.getBlockTime() == 0) {
            mazub.updateHitPoints((int) Constant.MAZUB_SLIME.getValue());
            slime.updateHitPoints((int) Constant.SLIME_MAZUB.getValue());
            mazub.setBlockTime(mazub.getTimeStep());
        }
    }

    @Override
    public void collideWithSneezewort(Sneezewort sneezewort) {
        if(sneezewort.isDead()) {
            mazub.updateHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
        }else if (mazub.getHitPoints() < mazub.getMaxHitPoints()){
            mazub.updateHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
            sneezewort.updateHitPoints(-1);
        }
        sneezewort.terminate();
    }

    @Override
    public void collideWithSpider(Spider spider) {
        if(mazub.isDead() || spider.isDead()) return;
        mazub.updateHitPoints((int) Constant.MAZUB_SPIDER.getValue());
        spider.updateHitPoints(-1);
    }
}
