package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.users.User;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.logging.Level;
import java.util.logging.Logger;

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
            loginLabel.setText(t.getMessage());
            loginLabel.setTextFill(Color.rgb(230, 40, 40));
        });
        loginService.setOnSucceeded(event -> System.out.println("User logged in " +
                loginService.getValue().getLogin()));
        loginService.start();
    }

    public void onSignUp() {
        Service<User> registerService = actionController.onSignUp(loginValue.getText(),
                passValue.getText(), asManager.isSelected());
        registerService.setOnFailed(event -> {
            Throwable t = registerService.exceptionProperty().get();
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, t.getMessage(), t);
            loginLabel.setText(t.getMessage());
            loginLabel.setTextFill(Color.rgb(230, 40, 40));
        });
        registerService.setOnSucceeded(event -> System.out.println("User successfully registered " +
                registerService.getValue().getLogin()));
        registerService.start();
    }
}
