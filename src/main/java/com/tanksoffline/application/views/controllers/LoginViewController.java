package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.services.ValidationService;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import org.hibernate.exception.ConstraintViolationException;

import javax.validation.ValidationException;
import java.util.Map;

public class LoginViewController {
    private UserActionController actionController;

    @FXML
    private Label loginLabel;

    @FXML
    private Label passLabel;

    @FXML
    private TextField loginValue;

    @FXML
    private PasswordField passValue;

    @FXML
    private CheckBox asManager;

    public LoginViewController() {
        this.actionController = new UserActionController();
    }

    public void changeField(KeyEvent event) {
        TextField field = (TextField) event.getSource();
        boolean isLogin = field == loginValue;
        Label label = (isLogin) ? loginLabel : passLabel;
        label.setText(isLogin ? "Ваш логин" : "Ваш пароль");
        label.setTextFill(Color.BLACK);
    }

    public void onLogin() {
        Service<User> loginService = actionController.onLogin(loginValue.getText(), passValue.getText());
        loginService.setOnFailed(event -> {
            Throwable t = loginService.exceptionProperty().get();
            ValidationService service = ServiceLocator.getInstance().getService(ValidationService.class);
            if (t instanceof ValidationException) {
                fireError();
            } else if (t instanceof IllegalStateException) {
                setError(loginLabel, service.getErrorMessage("incorrect_login"));
            } else if (t instanceof IllegalArgumentException) {
                setError(passLabel, service.getErrorMessage("incorrect_pass"));
            } else {
                throw new RuntimeException(t);
            }
        });
        loginService.setOnSucceeded(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Logged in");
            alert.setHeaderText(null);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.setContentText("Добро пожаловать " + loginService.getValue().getLogin());

            alert.showAndWait();
        });
        loginService.start();
    }

    public void onSignUp() {
        Service<User> registerService = actionController.onSignUp(loginValue.getText(),
                passValue.getText(), asManager.isSelected());
        registerService.setOnFailed(event -> {
            Throwable t = registerService.exceptionProperty().get();
            ValidationService service = ServiceLocator.getInstance().getService(ValidationService.class);
            if (t instanceof ValidationException) {
                fireError();
            } else if (t.getCause() instanceof ConstraintViolationException) {
                setError(loginLabel, service.getErrorMessage("login_exist"));
            } else {
                throw new RuntimeException(t);
            }
        });
        registerService.setOnSucceeded(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registered");
            alert.setHeaderText(null);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.setContentText("Вы успешно зарегистрировались под именем " + registerService.getValue().getLogin());

            alert.showAndWait();
        });
        registerService.start();
    }

    private void fireError() {
        ValidationService service = ServiceLocator.getInstance().getService(ValidationService.class);
        Map<String, String> errors = service.getErrorClasses();

        String message = errors.get("Login");
        if (message != null) {
            setError(loginLabel, message);
        }

        message = errors.get("Password");
        if (message != null) {
            setError(passLabel, message);
        }
    }

    private void setError(Label label, String message) {
        label.setText(message);
        label.setTextFill(Color.rgb(230, 40, 40));
    }
}
