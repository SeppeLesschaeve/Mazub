package jumpingalien.model.collision;

import jumpingalien.model.*;

public class SpiderCollider implements Collidable {

    private Spider spider;

    public SpiderCollider(Spider spider) {
        setSpider(spider);
    }

    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }

    @Override
    public void collideWithMazub(Mazub mazub) {
        if(mazub.isAdvancedByWorld()) return;
        mazub.updateHitPoints((int) Constant.MAZUB_SPIDER.getValue());
        spider.updateHitPoints(-1);
    }

    @Override
    public void collideWithShark(Shark shark) {
        if(shark.getSpiderTime() != 0) shark.setSpiderTime(shark.getSpiderTime() + spider.getTimeStep());
        if(shark.getSpiderTime() == 0) {
            spider.updateHitPoints(-(spider.getHitPoints()/2));
            shark.setSpiderTime(shark.getSpiderTime() + spider.getTimeStep());
        }
    }

    @Override
    public void collideWithSkullcab(Skullcab skullcab) {

    }

    @Override
    public void collideWithSlime(Slime slime) {
        if(slime.getSpiderTime() != 0) slime.setSpiderTime(slime.getSpiderTime() + spider.getTimeStep());
        if(slime.getSpiderTime() == 0) {
            spider.updateHitPoints(+1);
        }
    }

    @Override
    public void collideWithSneezewort(Sneezewort sneezewort) {

    }

    @Override
    public void collideWithSpider(Spider spider) {
        if(this.spider.isMovingHorizontally()) {
            if(this.spider.isMovingLeft()) {
                try {
                    spider.startMoveLeft();
                }catch (AssertionError ignored){}
                try {
                    this.spider.startMoveRight();
                }catch (AssertionError ignored){}
            }else {
                try {
                    this.spider.startMoveLeft();
                }catch (AssertionError ignored){}
                try {
                    spider.startMoveRight();
                }catch (AssertionError ignored){}
            }
        }
        if(this.spider.isMovingVertically()){
            if(this.spider.isFalling()) {
                try {
                    spider.startFall();
                }catch (IllegalStateException ignored){}
                try {
                    this.spider.startJump();
                }catch (IllegalStateException ignored){}
            }else {
                try {
                    this.spider.startFall();
                }catch (IllegalStateException ignored){}
                try {
                    spider.startJump();
                }catch (IllegalStateException ignored){}
            }
        }
    }
}
