package com.tanksoffline.core.validation;

import java.util.Map;

public class ValidationContextException extends RuntimeException {
    private Map<String, String> errorContext;

    public ValidationContextException(Map<String, String> errorContext) {
        this.errorContext = errorContext;
    }

    public Map<String, String> getErrors() {
        return errorContext;
    }
}
