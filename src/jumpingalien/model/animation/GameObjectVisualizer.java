package jumpingalien.model.animation;

import jumpingalien.model.Rectangle;
import jumpingalien.util.Sprite;

public abstract class GameObjectVisualizer {

    private Rectangle rectangle;
    private int currentImage;
    private Sprite[] images;

    protected GameObjectVisualizer(int xCoordinate, int yCoordinate, Sprite[] sprites){
        this.images = sprites.clone();
        this.currentImage = 0;
        this.rectangle = new Rectangle(xCoordinate, yCoordinate, getImageWidth(), getImageHeight());
    }

    public int getXCoordinate(){
        return rectangle.getXCoordinate();
    }

    public void setXCoordinate(int xCoordinate) {
        rectangle.setXCoordinate(xCoordinate);
    }

    public int getYCoordinate(){
        return rectangle.getYCoordinate();
    }

    public void setYCoordinate(int yCoordinate) {
        rectangle.setYCoordinate(yCoordinate);
    }

    public Sprite[] getImages() {
        return images.clone();
    }

    public Sprite getCurrentImage() {
        return images[currentImage];
    }

    public int getImageWidth() {
        return getCurrentImage().getWidth();
    }

    public int getImageHeight(){
        return getCurrentImage().getHeight();
    }

    public int getImage() {
        return currentImage;
    }

    public void setImage(int index) {
        this.currentImage = index;
        this.rectangle.setWidth(getImageWidth());
        this.rectangle.setHeight(getImageHeight());
    }

    public boolean overlaps(Rectangle rectangle) {
        return this.rectangle.overlaps(rectangle);
    }

    public abstract void updateImage();

    public Rectangle getRectangleOfImage(int index) {
        return new Rectangle(rectangle.getXCoordinate(), rectangle.getYCoordinate(),
                getImages()[index].getWidth(), getImages()[index].getHeight());
    }
}
