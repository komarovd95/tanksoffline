package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.utils.TaskFactory;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuViewController implements Initializable {
    private UserModel userModel;
    private UserActionController actionController;

    @FXML
    private Button usersBtn;

    public MenuViewController() {
        this.userModel = App.getInstance().getUserModel();
        this.actionController = new UserActionController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User loggedUser = userModel.getLoggedUser();
        if (!loggedUser.isManager()) {
            ((VBox) usersBtn.getParent()).getChildren().remove(usersBtn);
        }
    }

    public void onUsersClick() {
        Parent page = new App.ResourceFactory("/views/users.fxml").create();
        try {
            App.getInstance().replaceStageContent(page, () -> new Scene(page));
            App.getInstance().getPrimaryStage().setResizable(true);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    public void onLogoutClick() {
        new Service<User>() {
            @Override
            protected Task<User> createTask() {
                return new TaskFactory<>(actionController.onLogout()).create();
            }
        }.start();
    }

    public void onClose() {
        Platform.runLater(Platform::exit);
    }
}
