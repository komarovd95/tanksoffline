package com.tanksoffline.application.models.core.game;

import com.tanksoffline.core.utils.observer.Observable;

public interface Movable {
    Observable<Boolean> getIsMovingProperty();
    boolean isMoving();
    void move();
    void stopMoving();
    double getMovingSpeed();
}
