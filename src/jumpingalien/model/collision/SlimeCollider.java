package jumpingalien.model.collision;

import jumpingalien.model.*;

public class SlimeCollider implements Collidable {

    private Slime slime;

    public SlimeCollider(Slime slime) {
        setSlime(slime);
    }

    public Slime getSlime() {
        return slime;
    }

    public void setSlime(Slime slime) {
        this.slime = slime;
    }

    @Override
    public void collideWithMazub(Mazub mazub) {
        if(mazub.getBlockTime() == 0){
            slime.updateHitPoints((int) Constant.SLIME_MAZUB.getValue());
            mazub.setBlockTime(mazub.getBlockTime() + slime.getTimeStep());
            slime.synchronizeSchool();
        }
    }

    @Override
    public void collideWithShark(Shark shark) {
        if(slime.isDead() || shark.isDead()) return;
        slime.updateHitPoints(-slime.getHitPoints());
        slime.synchronizeSchool();
        shark.updateHitPoints((int) Constant.SHARK_SLIME.getValue());
    }

    @Override
    public void collideWithSkullcab(Skullcab skullcab) {

    }

    @Override
    public void collideWithSlime(Slime slime) {
        if(this.slime.isMovingLeft()) {
            try {
                slime.startMoveLeft();
            }catch (AssertionError ignored){}
            try {
                this.slime.startMoveRight();
            }catch (AssertionError ignored){}
        }else {
            try {
                this.slime.startMoveLeft();
            }catch (AssertionError ignored){}
            try {
                slime.startMoveRight();
            }catch (AssertionError ignored){}
        }
        if(slime.getSchool() == null || this.slime.getSchool() == null) return;
        if(slime.getSchool().getSlimes().size() < this.slime.getSchool().getSlimes().size()) {
            slime.setNewSchool(this.slime.getSchool());
        }else if(this.slime.getSchool().getSlimes().size() < slime.getSchool().getSlimes().size()) {
            this.slime.setNewSchool(slime.getSchool());
        }
    }

    @Override
    public void collideWithSneezewort(Sneezewort sneezewort) {

    }

    @Override
    public void collideWithSpider(Spider spider) {

    }
}
