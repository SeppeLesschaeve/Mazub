package jumpingalien.model;

public class SpiderSlimeHandler implements CollisionHandler {

	private Spider spider;
	private Slime slime;
	private double time;
	
	@Override
	public void handleCollision(Object colliderOne, Object colliderTwo, double dt) {
		if(colliderOne instanceof Spider && colliderTwo instanceof Slime) {
			setSpider((Spider) colliderOne); setSlime((Slime) colliderTwo);
		}else if(colliderOne instanceof Slime && colliderTwo instanceof Spider) {
			setSpider((Spider) colliderTwo); setSlime((Slime) colliderOne);
		}else {
			return; //not applicable
		}
		if(slime.isDead() || spider.isDead()) return;
		setTime(getTime() + dt);
		if(getTime() >= 0.5) {
			spider.setLegs(spider.getLegs() + 1);
			setTime(0.0);
		}
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public void setSpider(Spider spider) {
		this.spider = spider;
	}

	public void setSlime(Slime slime) {
		this.slime = slime;
	}

}
