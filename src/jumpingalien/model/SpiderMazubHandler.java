package jumpingalien.model;

public class SpiderMazubHandler implements CollisionHandler{

	private Mazub mazub;
	private Spider spider;
	
	@Override
	public void handleCollision(Object colliderOne, Object colliderTwo, double dt) {
		if(colliderOne instanceof Spider && colliderTwo instanceof Mazub) {
			setSpider((Spider) colliderOne); setMazub((Mazub) colliderTwo);
		}else if(colliderOne instanceof Mazub && colliderTwo instanceof Spider) {
			setSpider((Spider) colliderTwo); setMazub((Mazub) colliderOne);
		}else {
			return; //not applicable
		}
		if(spider.isDead() || mazub.isDead()) return;
		spider.setLegs(spider.getLegs() - 1);
		mazub.setHitPoints((int) Constant.MAZUB_SPIDER.getValue());
		if(spider.isRunning()) {
			spider.getVelocity().setX(0);
		}
		if(spider.isJumping() || spider.isFalling()) {
			spider.getVelocity().setY(0); 
			spider.getAcceleration().setY(0);
		}
	}

	private void setSpider(Spider spider) {
		this.spider = spider;
	}

	public void setMazub(Mazub mazub) {
		this.mazub = mazub;
	}
	
}
