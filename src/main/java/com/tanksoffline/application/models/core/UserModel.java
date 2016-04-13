package com.tanksoffline.application.models.core;

import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.utils.obs.Observable;
import com.tanksoffline.core.utils.validation.Login;
import com.tanksoffline.core.utils.validation.Password;

public interface UserModel extends DataModel<User> {
    void login(@Login String login, @Password String password);

    void register(@Login String login, @Password String password, boolean asManager);

    void logout();

    User getLoggedUser();

    Observable<User> getLoggedUserProperty();
}
