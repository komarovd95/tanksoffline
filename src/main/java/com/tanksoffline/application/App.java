package com.tanksoffline.application;

import com.tanksoffline.application.controllers.ApplicationController;
import com.tanksoffline.application.models.core.UserModel;
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
    private Stage currentStage;
    private ApplicationController applicationController;

    public static class ResourceFactory implements Factory<Parent> {
        private String resourceUrl;

        public ResourceFactory(String url) {
            this.resourceUrl = url;
        }

        @Override
        public Parent createItem() {
            try {
                return FXMLLoader.load(App.class.getResource(resourceUrl));
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    public App() {
        instance = this;
        applicationController = new ApplicationController();
        //userModel = new UserModel();
    }

    public static App getInstance() {
        return instance;
    }

    public static void setOnCenter(Stage stage) {
        final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
    }


    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        currentStage = primaryStage;
        applicationController.onStart();
    }

    public Parent replaceStageContent(Parent page, Factory<Scene> sceneFactory) throws Exception {
        Scene scene = currentStage.getScene();
        if (scene == null) {
            scene = sceneFactory.createItem();
            currentStage.setScene(scene);
        } else {
            currentStage.getScene().setRoot(page);
        }
        currentStage.sizeToScene();
        return page;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public UserModel getUserModel() {
        return ServiceLocator.getInstance().getService(DIService.class).getComponent(UserModel.class);
    }
}
