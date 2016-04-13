package com.tanksoffline.application.configuration;

import com.tanksoffline.application.controllers.ApplicationController;
import com.tanksoffline.application.controllers.NavigationController;
import com.tanksoffline.application.models.FieldModelImpl;
import com.tanksoffline.application.models.UserModelImpl;
import com.tanksoffline.application.models.core.FieldModel;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.application.utils.Navigation;
import com.tanksoffline.core.services.configuration.DIConfiguration;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.services.ValidationService;
import com.tanksoffline.core.utils.ProxyFactory;
import com.tanksoffline.core.utils.SingletonFactory;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDIConfiguration implements DIConfiguration {
    private static final ValidationService validationService = ServiceLocator.getInstance()
            .getService(ValidationService.class);
    private Map<String, Factory<?>> configurationMap;

    public ApplicationDIConfiguration() {
        configurationMap = new HashMap<>();
    }

    @Override
    public Map<String, Factory<?>> configure() {
        configurationMap.put(UserModel.class.getName(), new SingletonFactory<>(
                new ProxyFactory<>(UserModel.class,
                        validationService.createValidationHandler(new UserModelImpl())
                )
        ));

        configurationMap.put(FieldModel.class.getName(), new SingletonFactory<>(
                new ProxyFactory<>(FieldModel.class,
                        validationService.createValidationHandler(new FieldModelImpl())
                )
        ));

        configurationMap.put(ApplicationController.class.getName(),
                new SingletonFactory<>(ApplicationController::new));

        SingletonFactory<Navigation> navigationFactory = new SingletonFactory<>(NavigationController::new);
        configurationMap.put(Navigation.class.getName(), navigationFactory);
        configurationMap.put(NavigationController.class.getName(), navigationFactory);


        return configurationMap;
    }
}
