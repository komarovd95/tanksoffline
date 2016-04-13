package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.core.services.ValidationService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.validation.ValidationException;
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

    private UserActionController actionController;
    private User currentUser;
    private Stage currentStage;

    public ChangeUserViewController() {
        this.actionController = new UserActionController();
    }

    public void onAccept() {
        String passToken = (passValue.getText().equals("")) ? null : passValue.getText();
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new TaskFactory<Void>(() -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("password", passToken);
                    params.put("userType", isManager.isSelected());
                    actionController.onUpdate(currentUser, params).call();
                    return null;
                }).create();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                currentStage.getOnCloseRequest().handle(
                        new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }

            @Override
            protected void failed() {
                Throwable t = exceptionProperty().get();
                if (t instanceof ValidationException) {
                    ValidationService service = App.getService(ValidationService.class);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = App.getInstance().getNavigation().getNavigationInfo();
        passValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && currentStage == null) {
                currentStage = (Stage) passValue.getScene().getWindow();
                isManager.setSelected(currentUser.isManager());
            }
        });
    }
}
