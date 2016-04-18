package com.tanksoffline.core.services.configuration;

import com.tanksoffline.core.services.Service;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class DefaultServiceLocatorConfiguration implements ServiceLocatorConfiguration {
    protected Reflections reflections;

    public DefaultServiceLocatorConfiguration() {
        reflections = new Reflections("");
    }

    @Override
    public Map<Class<? extends Service>, Service> configure() {
        return configureServicesRecursively(Service.class);
    }

    private Map<Class<? extends Service>, Service> configureServicesRecursively(
            Class<? extends Service> serviceClass) {
        Map<Class<? extends Service>, Service> subServiceMap = new HashMap<>();
        Set<? extends Class<? extends Service>> subServiceClasses = reflections.getSubTypesOf(serviceClass);
        for (Class<? extends Service> c : subServiceClasses) {
            subServiceMap.put(c, null);
            subServiceMap.putAll(configureServicesRecursively(c));
        }
        return subServiceMap;
    }
}
