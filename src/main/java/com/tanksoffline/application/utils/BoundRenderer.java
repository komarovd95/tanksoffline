package com.tanksoffline.application.utils;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

@FunctionalInterface
public interface BoundRenderer<T> {
    void render(T t, GraphicsContext gc, Bounds bounds);
}
