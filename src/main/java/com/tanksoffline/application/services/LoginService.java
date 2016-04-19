package com.tanksoffline.application.services;

import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.core.services.Service;
import com.tanksoffline.core.utils.observer.Property;
import com.tanksoffline.core.utils.observer.SimpleProperty;

public class LoginService implements Service {
    private Property<User> loggedUserProperty;

    public Property<User> getLoggedUserProperty() {
        return loggedUserProperty;
    }

    public void signIn(User loggedUserEntity) {
        loggedUserProperty.set(loggedUserEntity);
    }

    public void signOut() {
        loggedUserProperty.set(null);
    }

    @Override
    public void start() {
        loggedUserProperty = new SimpleProperty<>();
    }

    @Override
    public void shutdown() {
        loggedUserProperty = null;
    }
}
