package com.tanksoffline.application.models.core;

import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.utils.validation.Login;
import com.tanksoffline.core.utils.validation.Password;
import javafx.beans.property.ObjectProperty;

public interface UserModel {
    void login(@Login String login, @Password String password);

    void register(@Login String login, @Password String password, boolean asManager);

    ObjectProperty<User> getLoggedUserProperty();
}
