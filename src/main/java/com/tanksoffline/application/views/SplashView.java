package com.tanksoffline.application.views;

import javafx.beans.NamedArg;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Service;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SplashView extends Scene {
    private Service<Void> serviceLoader;

    public SplashView(@NamedArg("root") Parent root, Service<Void> servicesLoader) {
        super(root);
        this.serviceLoader = servicesLoader;
        initialize();
    }

    private void initialize() {
        cursorProperty().bind(Bindings.when(serviceLoader.runningProperty())
                .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
        serviceLoader.start();
    }
}
