package com.tanksoffline.application.configuration;

import com.tanksoffline.core.services.ValidationService;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.Service;
import com.tanksoffline.application.services.HibernateDataService;
import com.tanksoffline.core.services.configuration.DefaultServiceLocatorConfiguration;
import com.tanksoffline.core.utils.ProxyFactory;
import com.tanksoffline.core.utils.SingletonFactory;

import java.util.HashMap;
import java.util.Map;

public class ApplicationServiceLocatorConfiguration extends DefaultServiceLocatorConfiguration {
    @Override
    public Map<Class<? extends Service>, Factory<? extends Service>> configureFactories() {
        Map<Class<? extends Service>, Factory<? extends Service>> factoryMap = new HashMap<>();
        Factory<DataService> hibernateDataServiceFactory =
                new SingletonFactory<>(
                        new ProxyFactory<>(DataService.class,
                                HibernateDataService.createTransactionHandler(new HibernateDataService())));
        factoryMap.put(DataService.class, hibernateDataServiceFactory);
        factoryMap.put(HibernateDataService.class, hibernateDataServiceFactory);
        factoryMap.put(DIService.class, new SingletonFactory<>(
                () -> new DIService(new ApplicationDIConfiguration())));
        factoryMap.put(ValidationService.class, new SingletonFactory<>(
                () -> new ValidationService(new ApplicationValidationConfiguration())));
        return factoryMap;
    }
}
