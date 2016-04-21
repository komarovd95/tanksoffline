package com.tanksoffline.application.app;

import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.application.controllers.NavigationController;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.models.game.GameModel;
import com.tanksoffline.application.services.LoginService;
import com.tanksoffline.application.utils.Navigation;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.Service;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.utils.observer.Property;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static <T> T getComponent(Class<T> c) {
        return ServiceLocator.getInstance().getService(DIService.class).getComponent(c);
    }

    public static <T> T getComponent(String componentName) {
        return ServiceLocator.getInstance().getService(DIService.class).getComponent(componentName);
    }

    public static <T extends Service> T getService(Class<T> c) {
        return ServiceLocator.getInstance().getService(c);
    }

    private Stage primaryStage;
    private ApplicationController applicationController;
    private Navigation navigationController;
    private GameModel gameModel;

    public App() {
        instance = this;

        applicationController = new ApplicationController();
        navigationController = new NavigationController();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(new Scene(new StackPane()));
        this.primaryStage.setMaximized(true);
        this.primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите покинуть игру?");
            alert.setHeaderText("Выход");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> Platform.exit());
        });

        applicationController.onStart();
    }

    @Override
    public void stop() throws Exception {
        getService(DataService.class).shutdown();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Property<User> getLoggedUserProperty() {
        return getService(LoginService.class).getLoggedUserProperty();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public ApplicationController getApplicationController() {
        return applicationController;
    }

    public StackPane getContentPane() {
        return (StackPane) primaryStage.getScene().getRoot();
    }

    public void setContent(List<? extends Node> content, String style) {
        StackPane contentPane = getContentPane();
        contentPane.getChildren().setAll(content);
        contentPane.setStyle(style);
    }

    public void setContent(Node content, String style, Pos position) {
        setContent(Collections.singletonList(content), style);
        StackPane.setAlignment(content, position);
    }

    public void setContent(Node content, String style, boolean fullSized) {
        if (fullSized) {
            AnchorPane anchorPane = new AnchorPane();
            setContent(Collections.singletonList(anchorPane), style);

            anchorPane.getChildren().add(content);

            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
        } else {
            setContent(Collections.singletonList(content), style);
        }
    }

    public Navigation getNavigation() {
        return navigationController;
    }

    public static void main(String[] args) {
//        try {
//
//        } catch (Throwable t) {
//            System.err.println(t.getMessage());
//            throw t;
//        }
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
        launch(args);
    }
}
