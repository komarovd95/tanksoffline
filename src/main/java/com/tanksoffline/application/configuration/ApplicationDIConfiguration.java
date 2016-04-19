package com.tanksoffline.application.configuration;

import com.tanksoffline.application.app.ApplicationController;
import com.tanksoffline.application.controllers.NavigationController;
import com.tanksoffline.application.utils.Navigation;
import com.tanksoffline.core.services.configuration.ServiceConfiguration;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.utils.SingletonFactory;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDIConfiguration implements ServiceConfiguration<String, Factory<?>> {
    private Map<String, Factory<?>> configurationMap;

    public ApplicationDIConfiguration() {
        configurationMap = new HashMap<>();
    }

    @Override
    public Map<String, Factory<?>> configure() {
        configurationMap.put(ApplicationController.class.getName(),
                new SingletonFactory<>(ApplicationController::new));

        SingletonFactory<Navigation> navigationFactory = new SingletonFactory<>(NavigationController::new);
        configurationMap.put(Navigation.class.getName(), navigationFactory);
        configurationMap.put(NavigationController.class.getName(), navigationFactory);
        configurationMap.put("NavigationComponent", navigationFactory);



//        configurationMap.put(UserModel.class.getName(), new SingletonFactory<>(
//                new ProxyFactory<>(UserModel.class,
//                        validationService.createValidationHandler(new UserModelImpl())
//                )
//        ));
//
//        configurationMap.put(FieldModel.class.getName(), new SingletonFactory<>(
//                new ProxyFactory<>(FieldModel.class,
//                        validationService.createValidationHandler(new FieldModelImpl())
//                )
//        ));


        return configurationMap;
    }
}
