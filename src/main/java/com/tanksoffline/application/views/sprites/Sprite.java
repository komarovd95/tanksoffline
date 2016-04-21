package com.tanksoffline.application.views.sprites;

import com.tanksoffline.core.utils.observer.Property;
import javafx.scene.Node;

public interface Sprite {
    Node getNode();
    double getCellSize();
    Property<Boolean> readyProperty();

    default boolean isReady() {
        return readyProperty().get();
    }

}
