package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.utils.TaskFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuViewController implements Initializable {
    private App app;
    private UserModel userModel;
    private UserActionController actionController;

    @FXML
    private Button usersBtn;

    public MenuViewController() {
        this.app = App.getInstance();
        this.userModel = app.getUserModel();
        this.actionController = new UserActionController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserEntity loggedUserEntity = userModel.getLoggedUser();
        if (!loggedUserEntity.isManager()) {
            ((VBox) usersBtn.getParent()).getChildren().remove(usersBtn);
        }
    }

    public void onUsersClick() {
        app.getApplicationController().onUsersView();
    }

    public void onLogoutClick() {
        new Service<UserEntity>() {
            @Override
            protected Task<UserEntity> createTask() {
                return new TaskFactory<>(actionController.onDestroy()).create();
            }
        }.start();
    }

    public void onCloseClick() {
        app.getPrimaryStage().getOnCloseRequest().handle(
                new WindowEvent(app.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onFieldsClick() {
        app.getApplicationController().onFieldsView();
    }

    public void onStartGameClick() {
        app.getApplicationController().onChooseFieldView();
    }
}
