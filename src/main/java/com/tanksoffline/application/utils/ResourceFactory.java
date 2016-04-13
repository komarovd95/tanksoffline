package com.tanksoffline.application.utils;

import com.tanksoffline.application.App;
import com.tanksoffline.core.utils.Factory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ResourceFactory implements Factory<Parent> {
    private String resourceUrl;

    public ResourceFactory(String url) {
        this.resourceUrl = url;
    }

    @Override
    public Parent create() {
        try {
            return FXMLLoader.load(App.class.getResource(resourceUrl));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FXMLLoader getLoader() {
        return new FXMLLoader(App.class.getResource(resourceUrl));
    }
}
