package com.tanksoffline.application.models.game;

import com.tanksoffline.core.utils.observer.Observable;

public interface Rotatable {
    Observable<Boolean> getIsRotatingProperty();
    boolean isRotating();
    void rotate(double degrees);
    void stopRotating();
    double getRotatingSpeed();
}
