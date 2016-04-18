package com.tanksoffline.core.validation;

import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.services.ValidationService;
import com.tanksoffline.core.utils.Builder;

import java.util.HashMap;
import java.util.Map;

public class ValidationContextBuilder implements Builder<ValidationContext> {
    private static final ValidationService validationService = ServiceLocator.getInstance()
            .getService(ValidationService.class);

    private Map<String, String> errorContext;
    private boolean isValid;

    private ValidationContextBuilder() {
        this.errorContext = new HashMap<>();
    }

    public static ValidationContextBuilder create() {
        return new ValidationContextBuilder();
    }

    public ValidationContextBuilder validate(String validatorName, Object obj) {
        ValidationContext context = validationService.getValidator(validatorName).validate(obj);
        errorContext.putAll(context.getErrorMessages());
        isValid &= context.isValid();
        return this;
    }

    @Override
    public ValidationContext build() {
        return new ValidationContext(errorContext, isValid);
    }
}
