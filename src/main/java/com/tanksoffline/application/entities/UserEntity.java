package com.tanksoffline.application.entities;

import com.tanksoffline.application.data.Match;
import com.tanksoffline.application.data.User;
import com.tanksoffline.core.data.DomainObject;

import java.util.List;

public class UserEntity extends DomainObject implements User {
    private String login;
    private String password;
    private UserType userType;
    private List<MatchEntity> matches;

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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o != null && o instanceof User) {
            User user = (User) o;
            return this.getLogin().equals(user.getLogin()) && this.getPassword().equals(user.getPassword())
                    && (this.isManager() == user.isManager());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (getLogin() == null ? 0 : getLogin().hashCode()) * 31
                + (getPassword() == null ? 0 : getLogin().hashCode()) * 31 + (isManager() ? 0 : 31);
    }
}
