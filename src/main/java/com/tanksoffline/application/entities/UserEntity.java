package com.tanksoffline.application.entities;

import com.tanksoffline.application.data.User;
import com.tanksoffline.core.data.DomainObject;

public class UserEntity extends DomainObject implements User {
    private String login;
    private String password;
    private UserType userType;

    public UserEntity() {}

    public UserEntity(String login, String password, UserType userType) {
        this.login = login;
        this.userType = userType;
        setPassword(password);
    }

    public UserEntity(String login, String password) {
        this(login, password, UserType.USER);
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public void setPassword(String password) {
        this.password = User.createPasswordDigest(password);
    }

    @Override
    public boolean isManager() {
        return userType == UserType.MANAGER;
    }

    @Override
    public UserType getUserType() {
        return userType;
    }

    @Override
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
