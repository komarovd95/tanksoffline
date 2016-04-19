package com.tanksoffline.application.controllers;

import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.search.UserSearch;
import com.tanksoffline.application.models.UserActiveModel;
import com.tanksoffline.application.services.LoginService;
import com.tanksoffline.core.data.Search;
import com.tanksoffline.core.mvc.ActionController;
import com.tanksoffline.core.services.ServiceLocator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class UserActionController implements ActionController<User> {
    private static final LoginService loginService = ServiceLocator.getInstance().getService(LoginService.class);

    private Search<User> userSearch;
    private UserActiveModel userActiveModel;

    public UserActionController() {}

    public UserActionController(User user) {
        userActiveModel = new UserActiveModel(user);
    }

    @Override
    public Callable<User> onCreate(Map<String, Object> values) {
        return () -> UserActiveModel.signUp((String) values.get("login"), (String) values.get("password"),
                ((boolean) values.get("userType")) ? User.UserType.MANAGER : User.UserType.USER);
    }

    @Override
    public Callable<User> onFind(Map<String, Object> values) {
        return () -> UserActiveModel.signIn((String) values.get("login"), (String) values.get("password"));
    }

    @Override
    public Callable<User> onUpdate(Map<String, Object> values) {
        return () -> {
            values.forEach((k, v) -> {
                switch (k) {
                    case "login":
                        userActiveModel.setLogin((String) v);
                        break;
                    case "password":
                        userActiveModel.setPassword((String) v);
                        break;
                    case "userType":
                        userActiveModel.setUserType(((boolean) v) ? User.UserType.MANAGER : User.UserType.USER);
                        break;
                }
            });
            return userActiveModel.update();
        };
    }

    @Override
    public Callable<User> onRemove() {
        return () -> userActiveModel.remove();
    }

    @Override
    public Callable<User> onFindOne(Object id) {
        return () -> userSearch.findOne(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Callable<List<User>> onFindAll() {
        return () -> (List<User>) userSearch.findAll();
    }

    @Override
    public Callable<User> onDestroy() {
        return () -> {
            User user = userActiveModel;
            userActiveModel = null;
            if (user == loginService.getLoggedUserProperty().get()) {
                loginService.signOut();
            }
            return user;
        };
    }

    @Override
    public Callable<User> onConstruct() {
        this.userSearch = new UserSearch();
        return () -> userActiveModel;
    }
}
