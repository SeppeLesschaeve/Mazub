package jumpingalien.model;

public class SpiderSharkHandler implements CollisionHandler{

	private Shark shark;
	private Spider spider;
	
	@Override
	public void handleCollision(Object colliderOne, Object colliderTwo, double dt) {
		if(colliderOne instanceof Spider && colliderTwo instanceof Shark) {
			setSpider((Spider) colliderOne); setShark((Shark) colliderTwo);
		}else if(colliderOne instanceof Shark && colliderTwo instanceof Spider) {
			setSpider((Spider) colliderTwo); setShark((Shark) colliderOne);
		}else {
			return; //not applicable
		}
		if(shark.isDead() || spider.isDead()) return;
		if(shark.getBlockTime() == 0) shark.setMoveTime(Constant.SHARK_SWITCH_TIME.getValue());
		if(spider.getBlockTime() == 0)spider.setLegs(spider.getLegs()/2);
		shark.setBlockTime(shark.getBlockTime() + dt);
		if(shark.getBlockTime() >= Constant.BLOCK.getValue()) {
			shark.setBlockTime(0.0);
		}
		spider.setBlockTime(spider.getBlockTime() + dt);
		if(spider.getBlockTime() >= Constant.BLOCK.getValue()) {
			spider.setBlockTime(0.0);
		}
		if(spider.isRunning()) {
			spider.getVelocity().setX(0);
		}
		if(spider.isJumping() || spider.isFalling()) {
			spider.getVelocity().setY(0); 
			spider.getAcceleration().setY(0);
		}
	}

	public void setSpider(Spider spider) {
		this.spider = spider;
	}

	public void setShark(Shark shark) {
		this.shark = shark;
	}
	
}
