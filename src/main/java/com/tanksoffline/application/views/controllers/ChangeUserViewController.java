package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.core.mvc.ActionController;
import com.tanksoffline.core.validation.ValidationContextException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ChangeUserViewController implements Initializable {
    @FXML
    private PasswordField passValue;

    @FXML
    private CheckBox isManager;

    @FXML
    private Label passLabel;

    private ActionController<User> actionController;
    private User currentUser;

    public ChangeUserViewController(User currentUser) {
        this.currentUser = currentUser;
        this.actionController = new UserActionController(currentUser);
    }

    public void onAccept() {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new TaskFactory<Void>(() -> {
                    Map<String, Object> params = new HashMap<>();
                    String passToken = ("".equals(passValue.getText())) ? null : passValue.getText().trim();
                    if (passToken != null) {
                        params.put("password", passToken);
                    }
                    params.put("userType", isManager.isSelected());
                    actionController.update(params).call();
                    return null;
                }).create();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                onBack();
            }

            @Override
            protected void failed() {
                Throwable t = exceptionProperty().get();
                if (t instanceof ValidationContextException) {
                    Map<String, String> errors = ((ValidationContextException) t).getErrors();

                    String message = errors.get("password");
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
        passValue.getScene().getWindow().getOnCloseRequest().handle(
                new WindowEvent(passValue.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onKeyTyped() {
        passLabel.setText("Новый пароль");
        passLabel.setTextFill(Color.BLACK);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isManager.setSelected(currentUser.isManager());
    }
}
