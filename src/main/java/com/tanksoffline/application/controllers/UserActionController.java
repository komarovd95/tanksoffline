package com.tanksoffline.application.controllers;

import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.core.mvc.ActionController;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.ServiceLocator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class UserActionController implements ActionController<UserEntity> {
    private UserModel userModel;

    public UserActionController() {
        userModel = ServiceLocator.getInstance().getService(DIService.class)
                .getComponent(UserModel.class);
    }

    @Override
    public Callable<UserEntity> onCreate(Map<String, Object> values) {
        return () -> {
            userModel.register((String) values.get("login"), (String) values.get("password"),
                    (boolean) values.get("userType"));
            return userModel.getLoggedUser();
        };
    }

    @Override
    public Callable<UserEntity> onFind(Map<String, Object> values) {
        return () -> {
            userModel.login((String) values.get("login"), (String) values.get("password"));
            return userModel.getLoggedUser();
        };
    }

    @Override
    public Callable<UserEntity> onFindOne(Object id) {
        return () -> userModel.findOne(id);
    }

    @Override
    public Callable<List<UserEntity>> onFindAll() {
        return () -> userModel.findAll();
    }

    @Override
    public Callable<UserEntity> onUpdate(UserEntity userEntity, Map<String, Object> values) {
        return () -> userModel.update(userEntity, values);
    }

    @Override
    public Callable<UserEntity> onDestroy() {
        return () -> {
            UserEntity loggedUserEntity = userModel.getLoggedUser();
            userModel.logout();
            return loggedUserEntity;
        };
    }

    @Override
    public Callable<UserEntity> onRemove(UserEntity userEntity) {
        return () -> {
            userModel.delete(userEntity);
            return null;
        };
    }

    @Override
    public Callable<UserEntity> onConstruct() {
        return null;
    }
}
