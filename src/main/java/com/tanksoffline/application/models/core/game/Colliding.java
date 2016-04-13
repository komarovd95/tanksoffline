package com.tanksoffline.application.models.core.game;

import com.tanksoffline.core.utils.obs.Observable;

public interface Colliding {
    Observable<Boolean> getIsCollidedProperty();
    boolean isCollided();
    void collide();
}
