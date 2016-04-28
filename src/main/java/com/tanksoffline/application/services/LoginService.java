package com.tanksoffline.application.services;

import com.tanksoffline.application.data.User;
import com.tanksoffline.core.services.Service;
import com.tanksoffline.core.utils.observer.Property;
import com.tanksoffline.core.utils.observer.SimpleProperty;

public class LoginService implements Service {
    private Property<User> loggedUserProperty;
    private boolean isStarted;

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
        isStarted = true;
    }

    @Override
    public void shutdown() {
        loggedUserProperty = null;
        isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }
}
