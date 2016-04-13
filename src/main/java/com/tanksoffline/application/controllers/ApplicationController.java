package com.tanksoffline.application.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.application.models.GameModelImpl;
import com.tanksoffline.application.models.core.game.GameModel;
import com.tanksoffline.application.tasks.ServiceLoader;
import com.tanksoffline.application.utils.ResourceFactory;
import com.tanksoffline.application.views.SplashView;
import com.tanksoffline.application.views.controllers.ChooseFieldViewController;
import com.tanksoffline.application.views.controllers.FieldsViewController;
import com.tanksoffline.application.views.controllers.GameViewController;
import com.tanksoffline.core.services.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.io.IOException;


public class ApplicationController {
    private App app;

    public ApplicationController() {
        app = App.getInstance();
    }

    public void onStart() throws Exception {
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        Parent splashRoot = new ResourceFactory("/views/splash.fxml").create();
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
    }

    public void onLoad() throws Exception {
        Stage loginStage = new Stage();
        loginStage.setTitle("TanksOffline");

        Parent loginRoot = new ResourceFactory("/views/login.fxml").create();
        loginStage.setScene(new Scene(loginRoot));
        loginStage.show();
        loginStage.setResizable(false);

        app.getUserModel().getLoggedUserProperty()
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
    }

    private void onMainScreen() throws Exception {
        Stage primaryStage = app.getPrimaryStage();
        primaryStage.setTitle("TanksOffline [" +
                app.getUserModel().getLoggedUser().getLogin() + "]");

        Parent root = new ResourceFactory("/views/menu.fxml").create();

        app.setContent(root,
                "-fx-background-image: url('/images/tank_wall.jpg'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;", Pos.CENTER);

        primaryStage.show();

        app.getNavigation().forward("MainMenu");
    }

    public void onUsersView() {
        Parent page = new ResourceFactory("/views/users.fxml").create();
        app.getNavigation().forward("UsersView");
        app.setContent(page, null, true);
    }

    public void onUserChange(User currentUser, Runnable updateCallback) {
        Stage changeStage = new Stage();
        changeStage.setTitle("Change user");
        changeStage.setResizable(false);

        app.getNavigation().setNavigationInfo(currentUser);

        ResourceFactory factory = new ResourceFactory("/views/change_user.fxml");
        changeStage.setScene(new Scene(factory.create()));

        changeStage.setOnCloseRequest(event -> {
            changeStage.close();
            updateCallback.run();
        });

        changeStage.show();
    }

    public void onFieldsView() {
        FXMLLoader loader = new ResourceFactory("/views/fields.fxml").getLoader();
        loader.setController(new FieldsViewController());
        app.getNavigation().forward("FieldsView");
        try {
            app.setContent(loader.load(), null, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onFieldCreate(Runnable updateCallback) {
        Stage createStage = new Stage();
        createStage.setTitle("Create field");
        createStage.setResizable(false);

        ResourceFactory factory = new ResourceFactory("/views/create_field.fxml");
        createStage.setScene(new Scene(factory.create()));

        createStage.setOnCloseRequest(event -> {
            createStage.close();
            updateCallback.run();
        });

        createStage.show();
    }

    public void onChooseFieldView() {
        FXMLLoader loader = new ResourceFactory("/views/fields.fxml").getLoader();
        loader.setController(new ChooseFieldViewController());
        app.getNavigation().forward("ChooseField");
        try {
            app.setContent(loader.load(), null, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void onGameView(Field field, Pair<Integer, Integer> playerSpawnCell,
                           Pair<Integer, Integer> enemySpawnCell) {
        GameModel gameModel = new GameModelImpl(field, playerSpawnCell.getKey(), playerSpawnCell.getValue(),
                enemySpawnCell.getKey(), enemySpawnCell.getValue());
        app.setGameModel(gameModel);

        FXMLLoader loader = new ResourceFactory("/views/game.fxml").getLoader();
        GameViewController gmc = new GameViewController();
        gmc.setScene(app.getPrimaryStage().getScene());
        loader.setController(new GameViewController());
        app.getNavigation().forward("GameView");
        try {
            app.setContent(loader.load(), null, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
