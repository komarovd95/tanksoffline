package com.tanksoffline.application.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.tasks.ServiceLoader;
import com.tanksoffline.application.views.SplashView;
import com.tanksoffline.core.services.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.Stack;

public class ApplicationController {
    private Stack<Parent> stageStack;
    private EventHandler<WindowEvent> closeHandler;
    private Stage currentStage;

    public ApplicationController() {
        stageStack = new Stack<>();
        closeHandler = event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите покинуть игру?");
            alert.setHeaderText("Выход");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> Platform.exit());
        };
    }

    public void onStart() throws Exception {
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        Parent splashRoot = new App.ResourceFactory("/views/splash.fxml").create();
        ServiceLoader servicesLoader = new ServiceLoader(
                DataService.class, ValidationService.class);
        servicesLoader.setOnSucceeded(event -> {
            try {
                splashStage.close();
                onLoad();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        splashStage.setScene(new SplashView(splashRoot, servicesLoader));
        splashStage.show();

        splashStage.centerOnScreen();
        currentStage = splashStage;
    }

    public void onLoad() throws Exception {
        Stage loginStage = new Stage();
        loginStage.setTitle("TanksOffline");

        Parent loginRoot = new App.ResourceFactory("/views/login.fxml").create();
        loginStage.setScene(new Scene(loginRoot));
        loginStage.setOnCloseRequest(closeHandler);
        loginStage.show();
        loginStage.setResizable(false);
        loginStage.setMaximized(false);

        App.getInstance().getUserModel().getLoggedUserProperty()
                .addObserver((observable, oldValue, newValue) ->
                        Platform.runLater(() -> {
                            if (newValue != null) {
                                try {
                                    loginStage.close();
                                    onMainScreen();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                App.getInstance().getPrimaryStage().hide();
                                loginStage.show();
                            }
                        })
                );

        loginStage.centerOnScreen();
        currentStage = loginStage;
    }

    private void onMainScreen() throws Exception {
        App app = App.getInstance();
        Stage primaryStage = app.getPrimaryStage();
        primaryStage.setTitle("TanksOffline [" + app.getUserModel().getLoggedUser().getLogin() + "]");

        Parent root = new App.ResourceFactory("/views/menu.fxml").create();
        app.replaceStageContent(root, () -> new Scene(root));

        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(closeHandler);
        primaryStage.show();
        currentStage = primaryStage;
        stageStack.push(root);
    }

    public void back() {
        try {
            App.getInstance().replaceStageContent(stageStack.peek(), null);
            currentStage = App.getInstance().getPrimaryStage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void forward(Parent parent) {
        stageStack.push(parent);
    }

    public Stage getCurrentStage() {
        return currentStage;
    }
}
