package jumpingalien.model;

public class SneezewortMazubHandler implements CollisionHandler {

	private Mazub mazub;

	@Override
	public void handleCollision(Object colliderOne, Object colliderTwo, double dt) {
		Plant plant;
		if(colliderOne instanceof Sneezewort && colliderTwo instanceof Mazub) {
			plant = (Sneezewort) colliderOne; setMazub((Mazub) colliderTwo);
		}else if(colliderOne instanceof Mazub && colliderTwo instanceof Sneezewort) {
			plant = (Sneezewort) colliderTwo; setMazub((Mazub) colliderOne);
		}else {
			return; //not applicable
		}
		if(mazub.getHitPoints() < mazub.getHitPoint().getMaximumAmountOfPoints() && mazub.getRectangle().overlaps(plant.getRectangle())) { 
				if(plant.getAge() < Plant.SNEEZE_AGE && plant.getHit() > 0) {
					mazub.setHitPoints((int) Constant.MAZUB_LIVING_PLANT.getValue());
				}else if(plant.getAge() >= Plant.SNEEZE_AGE && plant.getHit() > 0) {
						mazub.setHitPoints((int) Constant.MAZUB_DEAD_PLANT.getValue());
				}
				plant.terminate();
		}
	}

	private void setMazub(Mazub mazub) {
		this.mazub = mazub;
	}
	
}
