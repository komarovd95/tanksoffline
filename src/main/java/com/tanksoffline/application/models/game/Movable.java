package com.tanksoffline.application.models.game;

import com.tanksoffline.core.utils.observer.Observable;

public interface Movable {
    Observable<Boolean> getIsMovingProperty();
    boolean isMoving();
    void move();
    void stopMoving();
    double getMovingSpeed();
}
