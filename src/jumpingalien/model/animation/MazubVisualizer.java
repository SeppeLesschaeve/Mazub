package jumpingalien.model.animation;

import jumpingalien.model.Constant;
import jumpingalien.model.Mazub;
import jumpingalien.model.Rectangle;
import jumpingalien.util.Sprite;

public class MazubVisualizer extends GameObjectVisualizer {

    private Mazub mazub;
    private double spriteTime = 0.0;

    public MazubVisualizer(Mazub mazub, int xCoordinate, int yCoordinate, Sprite[] sprites) {
        super(xCoordinate, yCoordinate, sprites);
        this.setMazub(mazub);
    }

    public Mazub getMazub() {
        return mazub;
    }

    public void setMazub(Mazub mazub) {
        this.mazub = mazub;
    }

    public double getSpriteTime() {
        return spriteTime;
    }

    public void setSpriteTime(double spriteTime) {
        this.spriteTime = spriteTime;
    }

    @Override
    public void updateImage() {
        if(mazub.isJumping()){
            updateImageWhileJumping();
        }else if (mazub.isDucking()){
            updateImageWhileDucking();
        }else if (mazub.isMovingHorizontally() && mazub.getEndMoveTime() == 0){
            updateImageWhileRunning();
        }else if(!mazub.isMovingHorizontally()){
            mazub.setEndMoveTime(mazub.getEndMoveTime() + mazub.getTimeStep());
            if(mazub.getEndMoveTime() >= Constant.MAZUB_END_MOVE_TIME.getValue() && canChangeSprite(0)) {
                super.setImage(0);
                mazub.setEndMoveTime(0.0);
            }
        }
    }

    public void updateImageBeginJumping() {
        if(mazub.isMovingRight() && canChangeSprite(4)){
            setImage(4);
        }else if(mazub.isMovingLeft() && canChangeSprite(5)){
            setImage(5);
        }
        setSpriteTime(0.0);
    }

    public void updateImageWhileJumping() {
        if(mazub.isMovingRight() && canChangeSprite(4)){
            setImage(4);
        }else if(mazub.isMovingLeft() && canChangeSprite(5)){
            setImage(5);
        }
    }

    public void updateImageBeginDucking() {
        if(mazub.isMovingRight() && canChangeSprite(6)){
            setImage(6);
        }else if(mazub.isMovingLeft() && canChangeSprite(7)){
            setImage(7);
        }else if(canChangeSprite(1)){
            setImage(1);
        }
        setSpriteTime(0.0);
    }

    public void updateImageWhileDucking() {
        if(mazub.isMovingRight() && canChangeSprite(6)){
            setImage(6);
        }else if(mazub.isMovingLeft() && canChangeSprite(7)){
            setImage(7);
        }else if(canChangeSprite(1)){
            setImage(1);
        }
    }

    public boolean canStartDucking() {
        if(mazub.getOrientation() == 1){
            return canChangeSprite(6);
        }else if(mazub.getOrientation() == -1){
            return canChangeSprite(7);
        }else{
            return canChangeSprite(1);
        }
    }

    public boolean canStopDucking() {
        if(mazub.getOrientation() == 1){
            return canChangeSprite(8);
        }else if(mazub.getOrientation() == -1){
            return canChangeSprite(((getImages().length-8)/2)+8);
        }else{
            return canChangeSprite(0);
        }
    }

    public void updateImageAfterDucking(){
        if(mazub.getOrientation() == 1){
            setImage(8);
        }else if(mazub.getOrientation() == -1){
            setImage(((getImages().length-8)/2)+8);
        }else{
            setImage(0);
        }
    }

    public void updateImageBeginRunning(){
        if(mazub.getOrientation() == 1){
            setImage(8);
        }else if(mazub.getOrientation() == -1){
            setImage(((getImages().length-8)/2)+8);
        }
        setSpriteTime(0.0);
    }

    public void updateImageWhileRunning() {
        setSpriteTime(spriteTime + mazub.getTimeStep());
        int index = super.getImage() + 1;
        if(mazub.getOrientation() == 1 && index >= ((getImages().length-8)/2)+8){
            index = 8;
        }else if(mazub.getOrientation() == -1 && index >= getImages().length) {
            index = ((getImages().length-8)/2)+8;
        }
        if(getSpriteTime() >= 0.075 && canChangeSprite(index)) {
            setImage(index);
            setSpriteTime(spriteTime - 0.075);
        }
    }

    public void updateImageAfterRunning(){
        if(mazub.getOrientation() == 1){
            setImage(2);
        }else if(mazub.getOrientation() == -1){
            setImage(3);
        }
    }

    public boolean isStillMovingLeft() {
        return getImage() == 3;
    }

    public boolean isStillMovingRight() {
        return getImage() == 2;
    }

    public boolean canChangeSprite(int index) {
        return (mazub.getWorld() == null || (mazub.getWorld() != null && mazub.getCollidingCreatures(index).isEmpty()
                && mazub.getWorld().shallBePassable(new Rectangle(getXCoordinate(), getYCoordinate() + 1,
                getImages()[index].getWidth(), getImages()[index].getHeight() - 1))));
    }
}
