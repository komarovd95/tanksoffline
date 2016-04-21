package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.core.mvc.ActionController;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.utils.SingletonFactory;
import com.tanksoffline.core.validation.ValidationContextException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.PersistenceException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class LoginViewController implements Initializable {
    private ActionController<User> actionController;
    private Factory<Service<User>> loginServiceFactory;
    private Factory<Service<User>> registerServiceFactory;

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

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

    @FXML
    private ProgressIndicator progress;

    public LoginViewController() {
        this.actionController = new UserActionController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Runnable succeed = () -> {
            loginValue.setText("");
            loginLabel.setText("Ваш логин");
            loginLabel.setTextFill(Color.BLACK);

            passValue.setText("");
            passLabel.setText("Ваш пароль");
            passLabel.setTextFill(Color.BLACK);

            asManager.setSelected(false);

            brokeInterface(false);
        };

        loginServiceFactory = new SingletonFactory<>(() ->
                new Service<User>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    protected Task<User> createTask() {
                        Map<String, Object> params = new HashMap<>();
                        params.put("login", loginValue.getText().trim());
                        params.put("password", passValue.getText().trim());
                        return new TaskFactory<>((Callable<User>) actionController.findBy(params)).create();
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        Throwable t = this.exceptionProperty().get();
                        if (t instanceof ValidationContextException) {
                            fireError((ValidationContextException) t);
                        } else if (t instanceof IllegalStateException) {
                            setError(loginLabel, t.getMessage());
                        } else if (t instanceof  IllegalArgumentException) {
                            setError(passLabel, t.getMessage());
                        } else {
                            throw new RuntimeException(t);
                        }
                        brokeInterface(false);
                    }

                    @Override
                    protected void running() {
                        super.running();
                        brokeInterface(true);
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        succeed.run();
                    }
                }
        );

        registerServiceFactory = new SingletonFactory<>(() ->
                new Service<User>() {
                    @Override
                    protected Task<User> createTask() {
                        Map<String, Object> params = new HashMap<>();
                        params.put("login", loginValue.getText().trim());
                        params.put("password", passValue.getText());
                        params.put("userType", asManager.isSelected());
                        return new TaskFactory<>(actionController.create(params)).create();
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        Throwable t = this.exceptionProperty().get();
                        if (t instanceof ValidationContextException) {
                            fireError((ValidationContextException) t);
                        } else if (t.getCause() instanceof PersistenceException) {
                            setError(loginLabel, "Логин уже существует");
                        } else {
                            throw new RuntimeException(t);
                        }
                        brokeInterface(false);
                    }

                    @Override
                    protected void running() {
                        super.running();
                        brokeInterface(true);
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        succeed.run();
                    }
                }
        );
    }

    public void changeField(KeyEvent event) {
        TextField field = (TextField) event.getSource();
        boolean isLogin = field == loginValue;
        Label label = (isLogin) ? loginLabel : passLabel;
        label.setText(isLogin ? "Ваш логин" : "Ваш пароль");
        label.setTextFill(Color.BLACK);
    }

    public void onLogin() {
        Service<User> loginService = loginServiceFactory.create();
        if (loginService.getState() == Worker.State.READY) {
            loginService.start();
        } else {
            loginService.restart();
        }
    }

    public void onSignUp() {
        Service<User> registerService = registerServiceFactory.create();
        if (registerService.getState() == Worker.State.READY) {
            registerService.start();
        } else {
            registerService.restart();
        }
    }

    private void fireError(ValidationContextException e) {
        Map<String, String> errors = e.getErrors();

        String message = errors.get("login");
        if (message != null) {
            setError(loginLabel, message);
        }

        message = errors.get("password");
        if (message != null) {
            setError(passLabel, message);
        }
    }

    private void setError(Label label, String message) {
        label.setText(message);
        label.setTextFill(Color.rgb(230, 40, 40));
        label.getScene().getWindow().sizeToScene();
    }

    private void brokeInterface(boolean isBroken) {
        loginValue.setEditable(!isBroken);
        passValue.setEditable(!isBroken);
        asManager.setDisable(isBroken);

        loginBtn.setDisable(isBroken);
        registerBtn.setDisable(isBroken);

        progress.setVisible(isBroken);
    }
}
