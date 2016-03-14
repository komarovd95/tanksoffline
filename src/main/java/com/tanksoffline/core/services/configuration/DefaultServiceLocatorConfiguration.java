package com.tanksoffline.core.services.configuration;

import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.services.Service;
import com.tanksoffline.core.services.ValidationService;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultServiceLocatorConfiguration implements ServiceLocatorConfiguration {
    protected Reflections reflections;

    public DefaultServiceLocatorConfiguration() {
        reflections = new Reflections("");
    }

    @Override
    public Map<Class<? extends Service>, Service> configureServices() {
        return configureServicesRecursive(Service.class);
    }

    private Map<Class<? extends Service>, Service> configureServicesRecursive(
            Class<? extends Service> serviceClass) {
        Map<Class<? extends Service>, Service> subServiceMap = new HashMap<>();
        Set<? extends Class<? extends Service>> subServiceClasses = reflections.getSubTypesOf(serviceClass);
        for (Class<? extends Service> c : subServiceClasses) {
            subServiceMap.put(c, null);
            subServiceMap.putAll(configureServicesRecursive(c));
        }
        return subServiceMap;
    }

    @Override
    public Map<Class<? extends Service>, Factory<? extends Service>> configureFactories() {
        Map<Class<? extends Service>, Factory<? extends Service>> factoryMap = new HashMap<>();
        factoryMap.put(ValidationService.class, new Factory.SingletonFactory<>(ValidationService::new));
        return factoryMap;
    }
}
