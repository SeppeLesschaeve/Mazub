package jumpingalien.model;

public class SlimeSharkHandler implements CollisionHandler{

	private Shark shark;
	private Slime slime;

	@Override
	public void handleCollision(Object colliderOne, Object colliderTwo, double dt) {
		if(colliderOne instanceof Slime && colliderTwo instanceof Shark) {
			setSlime((Slime) colliderOne); setShark((Shark) colliderTwo);
		}else if(colliderOne instanceof Shark && colliderTwo instanceof Slime) {
			setSlime((Slime) colliderTwo); setShark((Shark) colliderOne);
		}else {
			return; //not applicable
		}
		if(shark.isDead() || slime.isDead()) return;
		if(slime.getBlockTime() == 0) slime.setHitPoints(-slime.getHitPoints());
		if(shark.getBlockTime() == 0) shark.setHitPoints((int) Constant.SHARK_SLIME.getValue());
		shark.setBlockTime(shark.getBlockTime() + dt);
		if(shark.getBlockTime() >= Constant.BLOCK.getValue()) {
			shark.setBlockTime(0.0);
		}
		slime.setBlockTime(slime.getBlockTime() + dt);
		if(slime.getBlockTime() >= Constant.BLOCK.getValue()) {
			slime.setBlockTime(0.0);
		}
	}

	public void setShark(Shark shark) {
		this.shark = shark;
	}
	
	public void setSlime(Slime slime) {
		this.slime = slime;
	}
	
}
