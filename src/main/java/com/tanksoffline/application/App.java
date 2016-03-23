package com.tanksoffline.application;

import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.application.controllers.ApplicationController;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.core.services.Service;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.ServiceLocator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    private static App instance;

    public static class ResourceFactory implements Factory<Parent> {
        private String resourceUrl;

        public ResourceFactory(String url) {
            this.resourceUrl = url;
        }

        @Override
        public Parent create() {
            try {
                return FXMLLoader.load(App.class.getResource(resourceUrl));
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    public static App getInstance() {
        return instance;
    }

    public static void setOnCenter(Stage stage) {
        final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
    }

    public static <T> T getComponent(Class<T> c) {
        return ServiceLocator.getInstance().getService(DIService.class)
                .getComponent(c);
    }

    public static <T extends Service> T getService(Class<T> c) {
        return ServiceLocator.getInstance().getService(c);
    }

    private Stage primaryStage;
    private ApplicationController applicationController;

    public App() {
        applicationController = ServiceLocator.getInstance().getService(DIService.class)
                .getComponent(ApplicationController.class);
        instance = this;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        applicationController.onStart();
    }

    public Parent replaceStageContent(Parent page, Factory<Scene> sceneFactory) throws Exception {
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            primaryStage.setScene(sceneFactory.create());
        } else {
            primaryStage.getScene().setRoot(page);
        }
        primaryStage.sizeToScene();
        return page;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public UserModel getUserModel() {
        return ServiceLocator.getInstance().getService(DIService.class).getComponent(UserModel.class);
    }

    public ApplicationController getApplicationController() {
        return applicationController;
    }

    public static void main(String[] args) {
        try {
            ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
            launch(args);
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }
}
