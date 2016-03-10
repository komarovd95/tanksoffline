package tanks.app.models;

import tanks.app.validators.LoginValidator;
import tanks.app.validators.PasswordValidator;
import tanks.app.validators.Validator;
import tanks.users.ManagerUser;
import tanks.users.User;
import tanks.utils.SecurityUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

public class UserModel extends AbstractDomainModel<User> {
    private final Validator<String> loginValidator;
    private final Validator<String> passwordValidator;
    private User currentUser;

    public UserModel() {
        loginValidator = new LoginValidator();
        passwordValidator = new PasswordValidator();
    }

    @Override
    public User fetch(User user) {
        if (user instanceof ManagerUser) {
            return fetch((ManagerUser) user, ManagerUser.class, "fields");
        }
        return update(user);
    }

    @Override
    public List<User> fetch(List<User> fetchList) {
        if (fetchList != null) {
            return fetchList.stream().map(this::fetch).collect(Collectors.toList());
        }
        return null;
    }

    public User login(String login, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        validate(login, password);
        List<User> userResultList = where("login", login).getResultList();
        if (userResultList.size() == 1) {
            User user = userResultList.get(0);
            String passwordDigest = SecurityUtil.getPasswordDigest(password);
            if (user.getPasswordDigest().equals(passwordDigest)) {
                currentUser = user;
                fireUpdate();
                return currentUser;
            } else {
                throw new RuntimeException("Incorrect password");
            }
        } else {
            throw new RuntimeException("Incorrect login");
        }
    }

    public User register(String login, String password) {
        validate(login, password);
        List<User> userResultList = where("login", login).getResultList();
        if (userResultList.size() == 0) {
            User user = new User(login, password);
            currentUser = save(user);
            return currentUser;
        } else {
            throw new RuntimeException("This login already exists");
        }
    }

    private void validate(String login, String password) {
        if (!loginValidator.isValid(login)) {
            throw new RuntimeException(loginValidator.getValidationMessage());
        } else if (!passwordValidator.isValid(password)) {
            throw new RuntimeException(passwordValidator.getValidationMessage());
        }
    }

    public User logout() {
        User wasLogged = currentUser;
        currentUser = null;
        return wasLogged;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isManager() {
        return currentUser instanceof ManagerUser;
    }
}
