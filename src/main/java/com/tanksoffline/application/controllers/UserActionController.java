package com.tanksoffline.application.controllers;

import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.ServiceLocator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class UserActionController implements ActionController<User> {
    private UserModel userModel;

    public UserActionController() {
        userModel = ServiceLocator.getInstance().getService(DIService.class)
                .getComponent(UserModel.class);
    }

    @Override
    public Callable<User> onCreate(Map<String, Object> values) {
        return () -> {
            userModel.register((String) values.get("login"), (String) values.get("password"),
                    (boolean) values.get("userType"));
            return userModel.getLoggedUser();
        };
    }

    @Override
    public Callable<User> onFind(Map<String, Object> values) {
        return () -> {
            userModel.login((String) values.get("login"), (String) values.get("password"));
            return userModel.getLoggedUser();
        };
    }

    @Override
    public Callable<User> onFindOne(Object id) {
        return () -> userModel.findOne(id);
    }

    @Override
    public Callable<List<User>> onFindAll() {
        return () -> userModel.findAll();
    }

    @Override
    public Callable<User> onUpdate(User user, Map<String, Object> values) {
        return () -> userModel.update(user, values);
    }

    @Override
    public Callable<User> onDestroy() {
        return () -> {
            User loggedUser = userModel.getLoggedUser();
            userModel.logout();
            return loggedUser;
        };
    }

    @Override
    public Callable<User> onRemove(User user) {
        return () -> {
            userModel.delete(user);
            return null;
        };
    }

    @Override
    public Callable<User> onConstruct() {
        return null;
    }
}
