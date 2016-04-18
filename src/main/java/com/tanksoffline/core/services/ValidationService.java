package com.tanksoffline.core.services;

import com.tanksoffline.core.services.configuration.ServiceConfiguration;
import com.tanksoffline.core.validation.Validator;

import java.util.Map;


public class ValidationService implements Service {
    private ServiceConfiguration<String, com.tanksoffline.core.validation.Validator<?>> configuration;
    private Map<String, Validator<?>> validators;

    public ValidationService(ServiceConfiguration<String, Validator<?>> configuration) {
        this.configuration = configuration;
    }

    @Override
    public void start() {
        validators = configuration.configure();
    }

    @Override
    public void shutdown() {
        validators = null;
    }

    @SuppressWarnings("unchecked")
    public <T> Validator<T> getValidator(String name) {
        if (!validators.containsKey(name)) {
            throw new IllegalArgumentException("Cannot look up validator with name " + name);
        }
        return (Validator<T>) validators.get(name);
    }
}
