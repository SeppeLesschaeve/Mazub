package jumpingalien.model.kinematics;

import jumpingalien.model.GameObject;

public interface Kinematics {

    double calculateNewTimeSlice(double dt, double time);
    void updatePosition(double dt, GameObject gameObject);
    boolean isStationary();
}
