package com.tanksoffline.application.models.core.game;

import com.tanksoffline.core.utils.obs.Observable;

public interface Rotatable {
    Observable<Boolean> getIsRotatingProperty();
    boolean isRotating();
    void rotate(double degrees);
    void stopRotating();
    double getRotatingSpeed();
}
