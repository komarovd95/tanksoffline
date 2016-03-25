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
    public void remove(User user) {
        user.remove();
    }

    @Override
    public void updatePassword(User user, @Password String password) {
        user.setPassword(password);
        user.update();
    }

    @Override
    public void updateUserType(User user, boolean asManager) {
        user.setUserType(loggedUser.get(), (asManager) ? UserType.MANAGER : UserType.USER);
        user.update();
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
    public List<User> findAll() {
        return dataService.findAll(User.class);
    }
}
