package com.tanksoffline.application.controllers;

import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.ServiceLocator;

import java.util.List;
import java.util.concurrent.Callable;

public class UserActionController {
    private UserModel userModel;

    public UserActionController() {
        userModel = ServiceLocator.getInstance().getService(DIService.class)
                .getComponent(UserModel.class);
    }

    public Callable<User> onLogin(String login, String password) {
        return () -> {
            userModel.login(login, password);
            return userModel.getLoggedUser();
        };
    }

    public Callable<User> onSignUp(String login, String password, boolean asManager) {
        return () -> {
            userModel.register(login, password, asManager);
            return userModel.getLoggedUser();
        };
    }

    public Callable<List<User>> onFindAll() {
        return () -> userModel.findAll();
    }

    public Callable<User> onLogout() {
        return () -> {
            User loggedUser = userModel.getLoggedUser();
            userModel.logout();
            return loggedUser;
        };
    }

    public Callable<User> onRemove(User user) {
        return () -> {
            userModel.remove(user);
            return null;
        };
    }

    public Callable<User> onUpdate(User user, String password, boolean isManager) {
        return () -> {
            if (password != null) {
                userModel.updatePassword(user, password);
            }
            userModel.updateUserType(user, isManager);
            return user;
        };
    }
}
