package com.tanksoffline.application.views.sprites;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;

public interface Sprite {
    double getMoveSpeed();
    double getRotatingSpeed();

    Image getImage();
    Bounds update(double timeElapsed);
}
