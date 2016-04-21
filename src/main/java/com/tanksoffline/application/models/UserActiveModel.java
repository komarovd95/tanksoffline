package com.tanksoffline.application.models;

import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.services.LoginService;
import com.tanksoffline.core.mvc.BaseModel;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.utils.observer.SimpleProperty;
import com.tanksoffline.core.validation.ValidationContext;
import com.tanksoffline.core.validation.ValidationContextBuilder;
import com.tanksoffline.core.validation.ValidationContextException;

import java.util.List;

public class UserActiveModel extends BaseModel<User> implements User {
    public UserActiveModel(String login, String password, UserType userType) {
        validation(login, password);
        this.modelProperty = new SimpleProperty<>(new UserEntity(login, password, userType));
    }

    public UserActiveModel(User user) {
        this.modelProperty = new SimpleProperty<>(user);
    }

    private static void validation(String login, String password) {
        ValidationContext context = ValidationContextBuilder.create()
                .validate("login", login)
                .validate("password", password)
                .build();
        if (!context.isValid()) {
            throw new ValidationContextException(context.getErrorMessages());
        }
    }

    @Override
    public String getLogin() {
        return modelProperty.get().getLogin();
    }

    @Override
    public void setLogin(String login) {
        ValidationContext context = ValidationContextBuilder.create()
                .validate("login", login)
                .build();
        if (!context.isValid()) {
            throw new ValidationContextException(context.getErrorMessages());
        }

        modelProperty.get().setLogin(login);
        modelProperty.set(modelProperty.get());
    }

    @Override
    public String getPassword() {
        return modelProperty.get().getPassword();
    }

    @Override
    public void setPassword(String password) {
        ValidationContext context = ValidationContextBuilder.create()
                .validate("password", password)
                .build();
        if (!context.isValid()) {
            throw new ValidationContextException(context.getErrorMessages());
        }

        modelProperty.get().setPassword(password);
        modelProperty.set(modelProperty.get());
    }

    @Override
    public UserType getUserType() {
        return modelProperty.get().getUserType();
    }

    @Override
    public void setUserType(UserType userType) {
        modelProperty.get().setUserType(userType);
        modelProperty.set(modelProperty.get());
    }

    @Override
    public boolean isManager() {
        return modelProperty.get().isManager();
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

    public static UserActiveModel signIn(String login, String password) {
        List<UserEntity> userEntities = dataService.findBy(UserEntity.class, "login", login);
        if (userEntities.size() == 1) {
            User userEntity = userEntities.get(0);
            if (userEntity.getPassword().equals(User.createPasswordDigest(password))) {
                UserActiveModel activeModel = new UserActiveModel(userEntity);
                ServiceLocator.getInstance().getService(LoginService.class).signIn(activeModel);
                return activeModel;
            } else {
                throw new IllegalArgumentException("Password is incorrect");
            }
        } else {
            throw new IllegalStateException("Given login doesn't exist");
        }
    }

    public static UserActiveModel signUp(String login, String password, UserEntity.UserType userType) {
        UserActiveModel userActiveModel = new UserActiveModel(login, password, userType);
        userActiveModel.save();
        ServiceLocator.getInstance().getService(LoginService.class).signIn(userActiveModel);
        return userActiveModel;
    }
}
