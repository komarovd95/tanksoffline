package com.tanksoffline.application.tasks;

import com.tanksoffline.application.App;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.data.users.User;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class RegisterService extends Service<User> {
    private String loginToken;
    private String passToken;
    private boolean asManager;

    public RegisterService(String login, String password, boolean asManager) {
        this.loginToken = login;
        this.passToken = password;
        this.asManager = asManager;
    }

    @Override
    protected Task<User> createTask() {
        return new Task<User>() {
            @Override
            protected User call() throws Exception {
                UserModel userModel = App.getInstance().getUserModel();
                userModel.register(loginToken, passToken, asManager);
                return userModel.getLoggedUserProperty().get();
            }
        };
    }
}
