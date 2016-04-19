package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.core.mvc.ActionController;
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
    private User user;
    private ActionController<User> actionController;

    @FXML
    private Button usersBtn;

    public MenuViewController() {
        this.app = App.getInstance();
        this.user = app.getLoggedUserProperty().get();
        this.actionController = new UserActionController(this.user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!user.isManager()) {
            ((VBox) usersBtn.getParent()).getChildren().remove(usersBtn);
        }
    }

    public void onUsersClick() {
        app.getApplicationController().onUsersView();
    }

    public void onLogoutClick() {
        new Service<User>() {
            @Override
            protected Task<User> createTask() {
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
