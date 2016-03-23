package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.services.ValidationService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.validation.ValidationException;
import java.util.Map;

public class ChangeUserViewController {
    @FXML
    private PasswordField passValue;

    @FXML
    private CheckBox isManager;

    @FXML
    private Label passLabel;

    private UserActionController actionController;
    private User currentUser;
    private Stage currentStage;

    public ChangeUserViewController() {
        this.actionController = new UserActionController();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public void onAccept() {
        String passToken = (passValue.getText().equals("")) ? null : passValue.getText();
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new TaskFactory<Void>(() -> {
                    actionController.onUpdate(currentUser, passToken, isManager.isSelected()).call();
                    return null;
                }).create();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                currentStage.close();
            }

            @Override
            protected void failed() {
                Throwable t = exceptionProperty().get();
                if (t instanceof ValidationException) {
                    ValidationService service = ServiceLocator.getInstance().getService(ValidationService.class);
                    Map<String, String> errors = service.getErrorClasses();

                    String message = errors.get("Password");
                    if (message != null) {
                        passLabel.setText(message);
                        passLabel.setTextFill(Color.rgb(230, 40, 40));
                    }
                } else {
                    throw new RuntimeException(t);
                }
            }
        }.start();
    }

    public void onBack() {
        currentStage.close();
    }

    public void onKeyTyped() {
        passLabel.setText("Новый пароль");
        passLabel.setTextFill(Color.BLACK);
    }
}
