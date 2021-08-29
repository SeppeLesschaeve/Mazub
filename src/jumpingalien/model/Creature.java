package jumpingalien.model;

import jumpingalien.util.Sprite;

public abstract class Creature extends Organism implements HorizontalMovable {

    protected Creature(int x, int y, int hitPoints, int minimum, int maximum, Sprite... sprites) throws IllegalArgumentException {
        super(x, y, hitPoints, minimum, maximum, sprites);
    }

    @Override
    public boolean isDead() {
        return getHitPoints() == 0;
    }

    @Override
    public void changeActualPosition(double[] newPosition) {
        if(getWorld() != null) {
            Rectangle newPlace = new Rectangle((int)(newPosition[0]/0.01), (int)(newPosition[1]/0.01), getImageWidth(), getImageHeight());
            if(!getWorld().shallBePassable(newPlace)) throw new IllegalArgumentException("You can not place the Game Object in Solid Ground");
        }
        super.changeActualPosition(newPosition);
    }

    public int getOrientation(){
        if(isMovingLeft()) return -1;
        if(isMovingRight()) return 1;
        return 0;
    }

    public void updateHorizontalMovement() {
        if(isMovingRight() && !canMoveRight()){
            for(GameObject gameObject : getCollidingCreatures(getRightBorder())){
                gameObject.accept(super.getCollider());
            }
            if(isMovingHorizontally() && !isDead())endMove();
        }
        if(isMovingLeft() && !canMoveLeft()){
            for(GameObject gameObject : getCollidingCreatures(getLeftBorder())){
                gameObject.accept(super.getCollider());
            }
            if(isMovingHorizontally() && !isDead())endMove();
        }
    }
}
