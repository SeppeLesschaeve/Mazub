package jumpingalien.model;

public class SlimeMazubHandler implements CollisionHandler{

	private Mazub mazub;
	private Slime slime;
	
	@Override
	public void handleCollision(Object colliderOne, Object colliderTwo, double dt) {
		if(colliderOne instanceof Slime && colliderTwo instanceof Mazub) {
			setSlime((Slime) colliderOne); setMazub((Mazub) colliderTwo);
		}else if(colliderOne instanceof Mazub && colliderTwo instanceof Slime) {
			setSlime((Slime) colliderTwo); setMazub((Mazub) colliderOne);
		}else {
			return; //not applicable
		}
		if(slime.isDead() || mazub.isDead()) return;
		if(slime.isRunning() && slime.getBlockTime() == 0) {
			slime.setHitPoints((int) Constant.SLIME_MAZUB.getValue());
			slime.synchronizeSchool();
		}
		if(mazub.isRunning() && mazub.getBlockTime() == 0) {
			mazub.setHitPoints((int) Constant.MAZUB_SLIME.getValue());
		}
		slime.setBlockTime(slime.getBlockTime() + dt);
		if(slime.getBlockTime() >= Constant.BLOCK.getValue()) {
			slime.setBlockTime(0.0);
		}
		mazub.setBlockTime(mazub.getBlockTime() + dt);
		if(mazub.getBlockTime() >= Constant.BLOCK.getValue()) {
			mazub.setBlockTime(0.0);
		}
	}

	public void setMazub(Mazub mazub) {
		this.mazub = mazub;
	}
	
	public void setSlime(Slime slime) {
		this.slime = slime;
	}

}
