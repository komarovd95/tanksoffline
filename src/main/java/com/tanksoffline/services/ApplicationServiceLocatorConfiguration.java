package com.tanksoffline.services;

import com.tanksoffline.application.utils.Factory;
import com.tanksoffline.services.core.DataService;
import com.tanksoffline.services.core.DefaultServiceLocatorConfiguration;
import com.tanksoffline.services.core.Service;

import java.util.HashMap;
import java.util.Map;

public class ApplicationServiceLocatorConfiguration extends DefaultServiceLocatorConfiguration {
    @Override
    public Map<Class<?>, Factory<? extends Service>> configureFactories() {
        Map<Class<?>, Factory<? extends Service>> factoryMap = new HashMap<>();
        Factory<HibernateDataService> hibernateDataServiceFactory =
                new Factory.SingletonFactory<>(HibernateDataService::new);
        factoryMap.put(DataService.class, hibernateDataServiceFactory);
        factoryMap.put(HibernateDataService.class, hibernateDataServiceFactory);

        return factoryMap;
    }
}
