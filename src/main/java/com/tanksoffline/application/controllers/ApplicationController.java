package com.tanksoffline.application.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.tasks.ServiceLoader;
import com.tanksoffline.application.views.LoginView;
import com.tanksoffline.application.views.SplashView;
import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.services.ValidationService;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ApplicationController {
    private UserModel userModel;

    public ApplicationController() {
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
    }

    public void onStart() throws Exception {
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        Parent splashRoot = new App.ResourceFactory("/views/splash.fxml").createItem();
        ServiceLoader servicesLoader = new ServiceLoader(
                DataService.class, ValidationService.class);
        servicesLoader.setOnSucceeded(event -> {
            try {
                splashStage.hide();
                onLoad();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        splashStage.setScene(new SplashView(splashRoot, servicesLoader));
        splashStage.show();

        App.setOnCenter(splashStage);
    }

    public void onLoad() throws Exception {
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.UTILITY);
        loginStage.setTitle("TanksOffline");

        Parent loginRoot = new App.ResourceFactory("/views/login.fxml").createItem();
        loginStage.setScene(new LoginView(loginRoot));
        loginStage.show();
        loginStage.setResizable(false);
        App.setOnCenter(loginStage);
    }

    public UserModel getUserModel() {
        return userModel;
    }
}
