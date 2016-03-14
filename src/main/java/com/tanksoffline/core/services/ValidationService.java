package com.tanksoffline.core.services;

import com.tanksoffline.core.services.Service;

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.InvocationHandler;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationService implements Service {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private ExecutableValidator executableValidator;
    private Set<ConstraintViolation<Object>> currentViolations;

    @Override
    public void start() {
        executableValidator = factory.getValidator().forExecutables();
    }

    @Override
    public void shutdown() {
        executableValidator = null;
    }

    public InvocationHandler createValidationHandler(Object validatedObject) {
        return (proxy, method, args) -> {
            currentViolations = executableValidator.validateParameters(validatedObject, method, args);
            if (currentViolations.size() > 0) {
                throw new ValidationException();
            }
            return method.invoke(validatedObject, args);
        };
    }

    public List<String> getErrorMessages() {
        return currentViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
    }
}
