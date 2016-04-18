package com.tanksoffline.application.configuration;

import com.tanksoffline.core.services.configuration.ServiceConfiguration;
import com.tanksoffline.core.validation.Validator;

import java.util.HashMap;
import java.util.Map;

public class ApplicationValidationConfiguration implements ServiceConfiguration<String, Validator<?>> {
    private Map<String, Validator<?>> validatorMap;

    public ApplicationValidationConfiguration() {
        this.validatorMap = new HashMap<>();
    }


    @Override
    public Map<String, Validator<?>> configure() {
        return validatorMap;
    }
}
