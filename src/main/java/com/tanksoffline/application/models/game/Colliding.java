package com.tanksoffline.application.models.game;

import com.tanksoffline.core.utils.observer.Observable;

public interface Colliding {
    Observable<Boolean> getIsCollidedProperty();
    boolean isCollided();
    void collide();
}
