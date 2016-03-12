package com.tanksoffline.services.core;

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
    public Map<Class<?>, Service> configureServices() {
        return configureServicesRecursive(Service.class);
    }

    private Map<Class<?>, Service> configureServicesRecursive(Class<? extends Service> serviceClass) {
        Map<Class<?>, Service> subServiceMap = new HashMap<>();
        Set<? extends Class<? extends Service>> subServiceClasses = reflections.getSubTypesOf(serviceClass);
        for (Class<? extends Service> c : subServiceClasses) {
            subServiceMap.put(c, null);
            subServiceMap.putAll(configureServicesRecursive(c));
        }
        return subServiceMap;
    }
}