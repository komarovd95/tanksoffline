package com.tanksoffline.application.models;

import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.utils.observer.Observable;
import com.tanksoffline.core.utils.observer.SimpleProperty;
import com.tanksoffline.core.utils.validation.Login;
import com.tanksoffline.core.utils.validation.Password;

import java.util.List;
import java.util.Map;

public class UserModelImpl implements UserModel {
    private final DataService dataService;
    private Observable<UserEntity> loggedUser;

    public UserModelImpl() {
        loggedUser = new SimpleProperty<>();
        dataService = ServiceLocator.getInstance().getService(DataService.class);
    }

    @Override
    public void login(@Login String login, @Password String password) {
        List<UserEntity> userEntityList = dataService.findBy(UserEntity.class, "login", login);
        if (userEntityList.size() == 1) {
            UserEntity userEntity = userEntityList.get(0);
            if (UserEntity.createPasswordDigest(password).equals(userEntity.getPassword())) {
                loggedUser.set(userEntity);
            } else {
                throw new IllegalArgumentException("Password is incorrect");
            }
        } else {
            throw new IllegalStateException("Login is incorrect");
        }
    }

    @Override
    public void register(@Login String login, @Password String password, boolean asManager) {
        UserEntity userEntity = new UserEntity(login, password, (asManager) ? UserEntity.UserType.MANAGER : UserEntity.UserType.USER);
        userEntity.save();
        loggedUser.set(userEntity);
    }

    @Override
    public void logout() {
        loggedUser.get().update();
        loggedUser.set(null);
    }

    @Override
    public UserEntity getLoggedUser() {
        return loggedUser.get();
    }

    @Override
    public Observable<UserEntity> getLoggedUserProperty() {
        return loggedUser;
    }

    @Override
    public UserEntity findOne(Object value) {
        return dataService.findById(UserEntity.class, value);
    }

    @Override
    public List<UserEntity> findAll() {
        return dataService.findAll(UserEntity.class);
    }

    @Override
    public List<UserEntity> findBy(Map<String, Object> params) {
        return dataService.findBy(UserEntity.class, params);
    }

    @Override
    public UserEntity update(UserEntity userEntity, Map<String, Object> values) {
        values.forEach((param, value) -> {
            switch (param) {
                case "password":
                    updatePassword(userEntity, (String) value);
                    break;
                case "userType":
                    updateUserType(userEntity, (boolean) value);
                    break;
            }
        });
        userEntity.update();
        return userEntity;
    }

    private void updatePassword(UserEntity userEntity, @Password String password) {
        userEntity.setPassword(password);
    }

    private void updateUserType(UserEntity userEntity, boolean asManager) {
        userEntity.setUserType(loggedUser.get(), (asManager) ? UserEntity.UserType.MANAGER : UserEntity.UserType.USER);
    }

    @Override
    public void delete(UserEntity userEntity) {
        userEntity.remove();
    }
}
