package jumpingalien.model.animation;

import jumpingalien.model.Spider;
import jumpingalien.model.animation.GameObjectVisualizer;
import jumpingalien.util.Sprite;

public class SpiderVisualizer extends GameObjectVisualizer {

    private Spider spider;

    public SpiderVisualizer(Spider spider, int xCoordinate, int yCoordinate, Sprite[] sprites) {
        super(xCoordinate, yCoordinate, sprites);
        setSpider(spider);
    }

    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }

    @Override
    public void updateImage() {
        if(spider.getOrientation() > 0) setImage(1);
        else if(spider.getOrientation() < 0) setImage(2);
        else setImage(0);
    }
}
