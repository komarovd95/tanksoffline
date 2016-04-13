package com.tanksoffline.application.models;

import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.utils.UserType;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.utils.obs.Observable;
import com.tanksoffline.core.utils.obs.SimpleObservable;
import com.tanksoffline.core.utils.validation.Login;
import com.tanksoffline.core.utils.validation.Password;

import java.util.List;
import java.util.Map;

public class UserModelImpl implements UserModel {
    private final DataService dataService;
    private Observable<User> loggedUser;

    public UserModelImpl() {
        loggedUser = new SimpleObservable<>();
        dataService = ServiceLocator.getInstance().getService(DataService.class);
    }

    @Override
    public void login(@Login String login, @Password String password) {
        List<User> userList = dataService.where(User.class, "login", login);
        if (userList.size() == 1) {
            User user = userList.get(0);
            if (User.getPasswordDigest(password).equals(user.getPassword())) {
                loggedUser.set(user);
            } else {
                throw new IllegalArgumentException("Password is incorrect");
            }
        } else {
            throw new IllegalStateException("Login is incorrect");
        }
    }

    @Override
    public void register(@Login String login, @Password String password, boolean asManager) {
        User user = new User(login, password, (asManager) ? UserType.MANAGER : UserType.USER);
        user.save();
        loggedUser.set(user);
    }

    @Override
    public void logout() {
        loggedUser.get().update();
        loggedUser.set(null);
    }

    @Override
    public User getLoggedUser() {
        return loggedUser.get();
    }

    @Override
    public Observable<User> getLoggedUserProperty() {
        return loggedUser;
    }

    @Override
    public User findOne(Object value) {
        return dataService.find(User.class, value);
    }

    @Override
    public List<User> findAll() {
        return dataService.findAll(User.class);
    }

    @Override
    public List<User> findBy(Map<String, Object> params) {
        return dataService.where(User.class, params);
    }

    @Override
    public User update(User user, Map<String, Object> values) {
        values.forEach((param, value) -> {
            switch (param) {
                case "password":
                    updatePassword(user, (String) value);
                    break;
                case "userType":
                    updateUserType(user, (boolean) value);
                    break;
            }
        });
        user.update();
        return user;
    }

    private void updatePassword(User user, @Password String password) {
        user.setPassword(password);
    }

    private void updateUserType(User user, boolean asManager) {
        user.setUserType(loggedUser.get(), (asManager) ? UserType.MANAGER : UserType.USER);
    }

    @Override
    public void delete(User user) {
        user.remove();
    }
}
