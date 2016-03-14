package com.tanksoffline.application.configuration;

import com.tanksoffline.application.controllers.ApplicationController;
import com.tanksoffline.application.models.UserModelImpl;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.services.ValidationService;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ApplicationDIConfiguration implements DIService.DIConfiguration {
    private Map<String, Factory<?>> configurationMap;

    public ApplicationDIConfiguration() {
        configurationMap = new HashMap<>();
    }

    @Override
    public Map<String, Factory<?>> configure() {
        configurationMap.put(UserModel.class.getName(),
                new Factory.SingletonFactory<>(() -> (UserModel) Proxy.newProxyInstance(
                        UserModel.class.getClassLoader(),
                        new Class[] {UserModel.class},
                        ServiceLocator.getInstance().getService(ValidationService.class)
                                .createValidationHandler(new UserModelImpl()))
                ));

        configurationMap.put(ApplicationController.class.getName(),
                new Factory.SingletonFactory<>(ApplicationController::new));


        return configurationMap;
    }
}
