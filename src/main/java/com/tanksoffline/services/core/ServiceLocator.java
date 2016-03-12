package com.tanksoffline.services.core;


import com.tanksoffline.application.utils.Factory;

import java.util.Map;

public class ServiceLocator {
    private static ServiceLocator locator;
    private static ServiceLocatorConfiguration configuration;

    private Map<Class<?>, Service> serviceMap;
    private Map<Class<?>, Factory<? extends Service>> factoryMap;

    private ServiceLocator() {
        serviceMap = configuration.configureServices();
        factoryMap = configuration.configureFactories();
    }

    public static synchronized void bind(ServiceLocatorConfiguration configuration) {
        ServiceLocator.configuration = configuration;
        locator = null;
    }

    public static ServiceLocator getInstance() {
        ServiceLocator localInstance = locator;
        if (localInstance == null) {
            synchronized (ServiceLocator.class) {
                localInstance = locator;
                if (localInstance == null) {
                    locator = localInstance = new ServiceLocator();
                }
            }
        }
        return localInstance;
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(Class<T> serviceClass) {
        if (serviceMap.containsKey(serviceClass)) {
            Service service = serviceMap.get(serviceClass);
            if (service == null) {
                Factory<? extends Service> factory = factoryMap.get(serviceClass);
                if (factory == null) {
                    throw new RuntimeException("Can't create a service " + serviceClass.getSimpleName());
                } else {
                    service = factory.createItem();
                    serviceMap.put(serviceClass, service);
                }
            }
            return (T) service;
        } else {
            throw new RuntimeException("Can't look up a service " + serviceClass.getSimpleName());
        }
    }
}
