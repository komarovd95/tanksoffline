package com.tanksoffline.application.tasks;

import com.tanksoffline.application.App;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.data.users.User;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoginService extends Service<User> {
    private String loginToken;
    private String passToken;

    public LoginService(String loginToken, String passToken) {
        this.loginToken = loginToken;
        this.passToken = passToken;
    }

    @Override
    protected Task<User> createTask() {
        return new Task<User>() {
            @Override
            protected User call() throws Exception {
                UserModel userModel = App.getInstance().getUserModel();
                userModel.login(loginToken, passToken);
                return userModel.getLoggedUserProperty().get();
            }
        };
    }
}
