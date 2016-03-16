package com.tanksoffline.core.services;

import com.tanksoffline.core.utils.UTF8Control;

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.InvocationHandler;
import java.util.*;
import java.util.stream.Collectors;

public class ValidationService implements Service {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    private ExecutableValidator executableValidator;
    private Set<ConstraintViolation<Object>> currentViolations;
    private ResourceBundle messagesBundle;

    @Override
    public void start() {
        Locale locale = new Locale("ru", "RU");
        messagesBundle = ResourceBundle.getBundle("ErrorMessages", locale, new UTF8Control());
        executableValidator = factory.getValidator().forExecutables();
    }

    @Override
    public void shutdown() {
        executableValidator = null;
    }

    public InvocationHandler createValidationHandler(Object validatedObject) {
        return (proxy, method, args) -> {
            if (args == null) args = new Object[0];
            currentViolations = executableValidator.validateParameters(validatedObject, method, args);
            if (currentViolations.size() > 0) {
                throw new ValidationException();
            }
            try {
                return method.invoke(validatedObject, args);
            } catch (Throwable t) {
                throw t.getCause();
            }

        };
    }

    public Map<String, String> getErrorClasses() {
        Map<String, String> map = new HashMap<>();
        currentViolations.stream().forEach(v -> map.put(
                v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(), v.getMessage()
        ));
        return map;
    }

    public List<String> getErrorMessages() {
        return currentViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
    }

    public String getErrorMessage(String errorCase) {
        return messagesBundle.getString(errorCase);
    }
}
