package com.tanksoffline.data.users;

import com.tanksoffline.application.utils.UserType;
import com.tanksoffline.data.core.ActiveRecord;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User extends ActiveRecord {
    private static final Logger logger = Logger.getLogger(User.class.getName());

    private String login;
    private String password;
    private UserType userType;

    public User() {}

    public User(String login, String password, UserType userType) {
        this.login = login;
        this.userType = userType;
        setPassword(password);
    }

    public User(String login, String password) {
        this(login, password, UserType.USER);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        try {
            if (password == null) {
                throw new Exception("Password can not be null");
            }
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            this.password = Base64.getEncoder().encodeToString(digest.digest());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public boolean isManager() {
        return userType == UserType.MANAGER;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(User manager, UserType userType) {
        if (manager.isManager()) {
            this.userType = userType;
        } else {
            throw new RuntimeException("Setting user type by simple user is not allowed");
        }
    }
}
