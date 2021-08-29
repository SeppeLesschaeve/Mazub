package jumpingalien.model.hit;

public class HitPoint {

    private int hitPoints;
    private final int minimum;
    private final int maximum;

    public HitPoint(int hitPoints, int minimum, int maximum) {
        if(minimum > maximum) minimum = maximum;
        this.minimum = minimum;
        this.maximum = maximum;
        setHitPoints(hitPoints);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void updateHitPoints(int hitPoints) {
        setHitPoints(getHitPoints() + hitPoints);
        roundHitPoints();
    }

    public void roundHitPoints(){
        if(getHitPoints() < getMinimum()) {
            setHitPoints(getMinimum());
        }
        if(getHitPoints() > getMaximum()) {
            setHitPoints(getMaximum());
        }
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

}
