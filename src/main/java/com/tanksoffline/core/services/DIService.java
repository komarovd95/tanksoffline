package com.tanksoffline.core.services;

import com.tanksoffline.core.services.configuration.ServiceConfiguration;
import com.tanksoffline.core.utils.Factory;

import java.util.Map;

public class DIService implements Service {
    private ServiceConfiguration<String, Factory<?>> configuration;
    private Map<String, Factory<?>> components;

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

    @Override
    public void start() {
        components = configuration.configure();
    }

    @Override
    public void shutdown() {
        components = null;
    }

}
