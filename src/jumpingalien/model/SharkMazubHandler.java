package jumpingalien.model;

public class SharkMazubHandler implements CollisionHandler{

	private Mazub mazub;
	private Shark shark;
	
	@Override
	public void handleCollision(Object colliderOne, Object colliderTwo, double dt) {
		if(colliderOne instanceof Shark && colliderTwo instanceof Mazub) {
			setShark((Shark) colliderOne); setMazub((Mazub) colliderTwo);
		}else if(colliderOne instanceof Mazub && colliderTwo instanceof Shark) {
			setShark((Shark) colliderTwo); setMazub((Mazub) colliderOne);
		}else {
			return; //not applicable
		}
		if(shark.isDead() || mazub.isDead()) return;
		if(mazub.getBlockTime() == 0) mazub.setHitPoints((int) Constant.MAZUB_SHARK.getValue());
		if(shark.getBlockTime() == 0) shark.setHitPoints((int) Constant.SHARK_MAZUB.getValue());
		shark.setBlockTime(shark.getBlockTime() + dt);
		if(shark.getBlockTime() >= Constant.BLOCK.getValue()) {
			shark.setBlockTime(0.0);
		}
		mazub.setBlockTime(mazub.getBlockTime() + dt);
		if(mazub.getBlockTime() >= Constant.BLOCK.getValue()) {
			mazub.setBlockTime(0.0);
		}
	}

	public void setMazub(Mazub mazub) {
		this.mazub = mazub;
	}
	
	public void setShark(Shark shark) {
		this.shark = shark;
	}
}
