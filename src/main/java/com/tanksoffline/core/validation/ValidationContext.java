package com.tanksoffline.core.validation;

import java.util.Map;

public class ValidationContext {
    private Map<String, String> errorMessages;
    private boolean isValid;

    public ValidationContext(Map<String, String> errorMessages, boolean isValid) {
        this.isValid = isValid;
        this.errorMessages = errorMessages;
    }

    public boolean isValid() {
        return isValid;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }
}
