package com.tanksoffline.application.models.core;

import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.core.utils.observer.Observable;
import com.tanksoffline.core.utils.validation.Login;
import com.tanksoffline.core.utils.validation.Password;

public interface UserModel extends DataModel<UserEntity> {
    void login(@Login String login, @Password String password);

    void register(@Login String login, @Password String password, boolean asManager);

    void logout();

    UserEntity getLoggedUser();

    Observable<UserEntity> getLoggedUserProperty();
}
