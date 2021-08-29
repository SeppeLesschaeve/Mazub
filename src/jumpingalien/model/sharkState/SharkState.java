package jumpingalien.model.sharkState;

public interface SharkState {

    double getTime();
    void setTime(double time);
    void next(double time);
    void move(double time);
    void initializeMovement();
    int getID();
}
