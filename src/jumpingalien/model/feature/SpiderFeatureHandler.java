package jumpingalien.model.feature;

import jumpingalien.model.Rectangle;
import jumpingalien.model.Spider;
import jumpingalien.model.feature.FeatureHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpiderFeatureHandler implements FeatureHandler {

    private Spider spider;
    private double featureTime;

    public SpiderFeatureHandler(Spider spider) {
        this.setSpider(spider);
    }

    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }

    public double getFeatureTime() {
        return featureTime;
    }

    public void setFeatureTime(double featureTime) {
        this.featureTime = featureTime;
        if(featureTime >= 0.6) this.featureTime = 0.0;
    }

    @Override
    public void handleFeature() {
        Rectangle innerRect = new Rectangle(spider.getXCoordinate(), spider.getYCoordinate(),
                spider.getImageWidth(),spider.getImageHeight());
        Rectangle outerRect = new Rectangle(innerRect.getXCoordinate()-1, innerRect.getYCoordinate()-1,
                innerRect.getWidth()+2,innerRect.getHeight()+2);
        if(spider.isInContactWithFeature(Feature.WATER, outerRect)) {
            handleWaterHit();
        }
        if(spider.isInContactWithFeature(Feature.ICE, outerRect)) {
            handleIceHit();
        }
        if(spider.isInContactWithFeature(Feature.MAGMA, innerRect)){
            handleMagmaHit();
        }else {
            setFeatureTime(0.0);
        }
    }

    @Override
    public void handleGasHit() {

    }

    @Override
    public void handleIceHit() {
        spider.terminate();
    }

    @Override
    public void handleMagmaHit() {
        if(featureTime == 0.0) {
            spider.updateHitPoints(-2);
        }
        setFeatureTime(featureTime + spider.getTimeStep());
    }

    @Override
    public void handleWaterHit() {
        spider.terminate();
    }
}

