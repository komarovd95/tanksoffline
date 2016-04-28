package com.tanksoffline.core.services;

import com.tanksoffline.core.services.configuration.ServiceConfiguration;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.utils.SingletonFactory;

import java.util.Map;

public class DIService implements Service {
    private ServiceConfiguration<String, Factory<?>> configuration;
    private Map<String, Factory<?>> components;
    private boolean isStarted;

    public DIService(ServiceConfiguration<String, Factory<?>> configuration) {
        this.configuration = configuration;
    }

    public <T> T getComponent(Class<T> componentClass) {
        return getComponent(componentClass.getName());
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(String componentName) {
        if (!components.containsKey(componentName)) {
            throw new IllegalArgumentException("Cannot look up component with name " + componentName);
        }
        return (T) components.get(componentName).create();
    }

    public void addComponent(String componentName, Factory<?> componentProducer) {
        components.put(componentName, componentProducer);
    }

    public void addComponent(String componentName, Object singletonComponent) {
        addComponent(componentName, new SingletonFactory<>(() -> singletonComponent));
    }

    @Override
    public void start() {
        components = configuration.configure();
        isStarted = true;
    }

    @Override
    public void shutdown() {
        components = null;
        isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }
}
