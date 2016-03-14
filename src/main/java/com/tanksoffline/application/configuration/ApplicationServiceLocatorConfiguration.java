package com.tanksoffline.application.configuration;

import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.Service;
import com.tanksoffline.application.services.HibernateDataService;
import com.tanksoffline.core.services.configuration.DefaultServiceLocatorConfiguration;

import java.util.Map;

public class ApplicationServiceLocatorConfiguration extends DefaultServiceLocatorConfiguration {
    @Override
    public Map<Class<? extends Service>, Factory<? extends Service>> configureFactories() {
        Map<Class<? extends Service>, Factory<? extends Service>> factoryMap = super.configureFactories();
        Factory<HibernateDataService> hibernateDataServiceFactory =
                new Factory.SingletonFactory<>(HibernateDataService::new);
        factoryMap.put(DataService.class, hibernateDataServiceFactory);
        factoryMap.put(HibernateDataService.class, hibernateDataServiceFactory);
        factoryMap.put(DIService.class, new Factory.SingletonFactory<>(
                () -> new DIService(new ApplicationDIConfiguration())
        ));
        return factoryMap;
    }
}
