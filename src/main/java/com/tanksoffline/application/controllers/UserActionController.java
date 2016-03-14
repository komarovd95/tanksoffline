package com.tanksoffline.application.controllers;

import com.tanksoffline.application.tasks.LoginService;
import com.tanksoffline.application.tasks.RegisterService;
import com.tanksoffline.application.data.users.User;
import javafx.concurrent.Service;

public class UserActionController {
    public Service<User> onLogin(String login, String password) {
        return new LoginService(login, password);
    }
    public Service<User> onSignUp(String login, String password, boolean asManager) {
        return new RegisterService(login, password, asManager);
    }
    public void onLogout() {}
}
