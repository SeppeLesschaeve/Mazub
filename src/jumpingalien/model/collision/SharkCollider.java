package jumpingalien.model.collision;

import jumpingalien.model.*;
import jumpingalien.model.collision.Collidable;

public class SharkCollider implements Collidable {

    private Shark shark;

    public SharkCollider(Shark shark) {
        setShark(shark);
    }

    public Shark getShark() {
        return shark;
    }

    public void setShark(Shark shark) {
        this.shark = shark;
    }

    @Override
    public void collideWithMazub(Mazub mazub) {
        if(mazub.getBlockTime() == 0){
            mazub.updateHitPoints((int) Constant.MAZUB_SHARK.getValue());
            shark.updateHitPoints((int) Constant.SHARK_MAZUB.getValue());
            mazub.setBlockTime(mazub.getBlockTime() + shark.getTimeStep());
        }
    }

    @Override
    public void collideWithShark(Shark shark) {
        if(this.shark.isMovingVertically())this.shark.endJump();
        if(this.shark.isMovingHorizontally())this.shark.endMove();
        if(shark.isMovingVertically())shark.endJump();
        if(shark.isMovingHorizontally())shark.endMove();
    }

    @Override
    public void collideWithSkullcab(Skullcab skullcab) {

    }

    @Override
    public void collideWithSlime(Slime slime) {
        if(slime.isDead() || shark.isDead()) return;
        slime.updateHitPoints(-slime.getHitPoints());
        slime.synchronizeSchool();
        shark.updateHitPoints((int) Constant.SHARK_SLIME.getValue());
    }

    @Override
    public void collideWithSneezewort(Sneezewort sneezewort) {

    }

    @Override
    public void collideWithSpider(Spider spider) {

    }
}
