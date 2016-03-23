package com.tanksoffline.application.models.core;

import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.utils.obs.Observable;
import com.tanksoffline.core.utils.validation.Login;
import com.tanksoffline.core.utils.validation.Password;

import java.util.List;

public interface UserModel {
    void login(@Login String login, @Password String password);

    void register(@Login String login, @Password String password, boolean asManager);

    void logout();

    void remove(User user);

    void updatePassword(User user, @Password String password);

    void updateUserType(User user, boolean asManager);

    User getLoggedUser();
    Observable<User> getLoggedUserProperty();

    List<User> findAll();
}
