package jumpingalien.model;

public class SkullcabMazubHandler implements CollisionHandler {
	
	private Mazub mazub;

	@Override
	public void handleCollision(Object colliderOne, Object colliderTwo, double dt) {
		Plant plant;
		if(colliderOne instanceof Skullcab && colliderTwo instanceof Mazub) {
			plant = (Skullcab) colliderOne; setMazub((Mazub) colliderTwo);
		}else if(colliderOne instanceof Mazub && colliderTwo instanceof Skullcab) {
			plant = (Skullcab) colliderTwo; setMazub((Mazub) colliderOne);
		}else {
			return; //not applicable
		}
		if(!plant.getRectangle().overlaps(mazub.getRectangle())) {
			plant.setHitTime(0); return;
		}
		if(plant.getHitTime() == 0 && plant.getHit() != 0 && mazub.getHitPoints() < mazub.getHitPoint().getMaximumAmountOfPoints() ) {
			if(plant.isDead()) {
				mazub.setHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
			}else {
				mazub.setHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
			}
			plant.getHitPoint().setPoints(-1);
		}
		plant.setHitTime(plant.getHitTime() + dt);
		if(plant.getHitTime()  >= 0.6) {
			plant.setHitTime(0);
		}
		if(plant.getHit() == 0) plant.terminate();
	}

	private void setMazub(Mazub mazub) {
		this.mazub = mazub;
	}

}
