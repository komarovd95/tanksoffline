package com.tanksoffline.application.models;

import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.core.mvc.ActiveModel;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.utils.observer.Observer;
import com.tanksoffline.core.utils.observer.Property;
import com.tanksoffline.core.utils.observer.SimpleProperty;
import com.tanksoffline.core.validation.ValidationContext;
import com.tanksoffline.core.validation.ValidationContextBuilder;
import com.tanksoffline.core.validation.ValidationContextException;

import java.util.List;

public class UserActiveModel extends UserEntity implements ActiveModel<UserEntity> {
    private static final DataService dataService = ServiceLocator.getInstance().getService(DataService.class);

    private Property<UserEntity> userProperty;

    public UserActiveModel(String login, String password, UserType userType) {
        ValidationContext context = ValidationContextBuilder.create()
                .validate("login", login)
                .validate("password", password)
                .build();
        if (!context.isValid()) {
            throw new ValidationContextException(context.getErrorMessages());
        }

        userProperty = new SimpleProperty<>(new UserEntity(login, password, userType));
    }

    public UserActiveModel(UserEntity userEntity) {
        this.userProperty = new SimpleProperty<>(userEntity);
    }

    @Override
    public UserEntity save() {
        userProperty.set(dataService.save(userProperty.get()));
        return userProperty.get();
    }

    @Override
    public UserEntity update() {
        userProperty.set(dataService.update(userProperty.get()));
        return userProperty.get();
    }

    @Override
    public UserEntity remove() {
        userProperty.set(dataService.remove(userProperty.get()));
        return userProperty.get();
    }

    @Override
    public UserEntity refresh() {
        userProperty.set(dataService.refresh(userProperty.get()));
        return userProperty.get();
    }

    @Override
    public void addObserver(Observer<? super UserEntity> observer) {
        userProperty.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer<? super UserEntity> observer) {
        userProperty.removeObserver(observer);
    }

    @Override
    public String getLogin() {
        return userProperty.get().getLogin();
    }

    @Override
    public void setLogin(String login) {
        userProperty.get().setLogin(login);
        userProperty.set(userProperty.get());
    }

    @Override
    public String getPassword() {
        return userProperty.get().getPassword();
    }

    @Override
    public void setPassword(String password) {
        userProperty.get().setPassword(password);
        userProperty.set(userProperty.get());
    }

    @Override
    public UserType getUserType() {
        return userProperty.get().getUserType();
    }

    @Override
    public void setUserType(UserType userType) {
        userProperty.get().setUserType(userType);
        userProperty.set(userProperty.get());
    }

    @Override
    public boolean isManager() {
        return userProperty.get().isManager();
    }

    public static ActiveModel<UserEntity> signIn(String login, String password) {
        List<UserEntity> userEntities = dataService.findBy(UserEntity.class, "login", login);
        if (userEntities.size() == 1) {
            UserEntity userEntity = userEntities.get(0);
            if (userEntity.getPassword().equals(UserEntity.createPasswordDigest(password))) {
                return new UserActiveModel(userEntity);
            } else {
                throw new IllegalArgumentException("Password is incorrect");
            }
        } else {
            throw new IllegalStateException("Given login doesn't exist");
        }
    }

    public static ActiveModel<UserEntity> signUp(String login, String password, UserEntity.UserType userType) {
        ActiveModel<UserEntity> userActiveModel = new UserActiveModel(login, password, userType);
        userActiveModel.save();
        return userActiveModel;
    }
}
