package com.tanksoffline.core.services;


import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.services.configuration.ServiceLocatorConfiguration;

import java.util.Map;

public class ServiceLocator {
    private static ServiceLocator locator;
    private static ServiceLocatorConfiguration configuration;

    public static synchronized void bind(ServiceLocatorConfiguration configuration) {
        ServiceLocator.configuration = configuration;
        locator = null;
        getInstance();
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

    private Map<Class<? extends Service>, Service> serviceMap;
    private Map<Class<? extends Service>, Factory<? extends Service>> factoryMap;

    private ServiceLocator() {
        serviceMap = configuration.configure();
        factoryMap = configuration.configureFactories();
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(Class<T> serviceClass) {
        loadService(serviceClass);
        return (T) serviceMap.get(serviceClass);
    }

    public void loadService(Class<? extends Service> serviceClass) {
        if (serviceMap.containsKey(serviceClass)) {
            Service service = serviceMap.get(serviceClass);
            if (service == null) {
                Factory<? extends Service> factory = factoryMap.get(serviceClass);
                if (factory == null) {
                    throw new RuntimeException("Cannot create a service " + serviceClass.getSimpleName());
                } else {
                    System.out.println("Service loading was started " + serviceClass);
                    service = factory.create();
                    service.start();
                    serviceMap.put(serviceClass, service);
                    System.out.println("Service was started " + serviceClass);
                }
            }
        } else {
            throw new RuntimeException("Cannot look up a service " + serviceClass.getSimpleName());
        }
    }

    public void loadServices() {
        serviceMap.keySet().forEach(this::loadService);
    }
}
