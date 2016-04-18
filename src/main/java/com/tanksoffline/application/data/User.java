package com.tanksoffline.application.data;

import java.security.MessageDigest;
import java.util.Base64;

public interface User {
    String getLogin();
    void setLogin(String login);

    String getPassword();
    void setPassword(String password);

    UserType getUserType();
    void setUserType(UserType userType);
    boolean isManager();

    static String createPasswordDigest(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            return Base64.getEncoder().encodeToString(digest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    enum UserType {
        USER, MANAGER
    }
}
