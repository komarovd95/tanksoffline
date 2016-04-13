package com.tanksoffline.application.utils;

import javafx.scene.canvas.GraphicsContext;

@FunctionalInterface
public interface Renderer<T> {
    void render(T t, GraphicsContext gc);
}
