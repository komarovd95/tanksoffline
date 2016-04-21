package com.tanksoffline.application.configuration;

import com.tanksoffline.core.services.configuration.ServiceConfiguration;
import com.tanksoffline.core.validation.ValidationContext;
import com.tanksoffline.core.validation.Validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ApplicationValidationConfiguration implements ServiceConfiguration<String, Validator<?>> {
    @Override
    public Map<String, Validator<?>> configure() {
        Map<String, Validator<?>> validatorMap = new HashMap<>();

        validatorMap.put("login", (Validator<String>) s -> {
            if (s == null || s.length() == 0) {
                return new ValidationContext(Collections.singletonMap("login", "Логин не может быть пустым"), false);
            } else if (s.length() < 4) {
                return new ValidationContext(Collections.singletonMap("login",
                        "Длина логина должна быть не меньше 4 символов"), false);
            } else if (!s.matches("[a-zA-z]\\w+")) {
                return new ValidationContext(Collections.singletonMap("login", "Логин должен начинаться с буквы"), false);
            } else {
                return new ValidationContext(Collections.emptyMap(), true);
            }
        });

        validatorMap.put("password", (Validator<String>) s -> {
            if (s == null || s.length() == 0) {
                return new ValidationContext(Collections.singletonMap("password", "Пароль не может быть пустым"), false);
            } else if (s.length() < 6) {
                return new ValidationContext(Collections.singletonMap("password",
                        "Длина пароля должна быть не меньше 6 символов"), false);
            } else if (!s.matches("^[a-zA-Z0-9]+$")) {
                return new ValidationContext(Collections.singletonMap("password", "Пароль не должен содержать пробелов"), false);
            } else {
                return new ValidationContext(Collections.emptyMap(), true);
            }
        });

        validatorMap.put("fieldName", (Validator<String>) s -> {
            if (s == null || s.length() == 0) {
                return new ValidationContext(Collections.singletonMap("name", "Имя поля не может быть пустым"), false);
            } else if (s.length() < 4) {
                return new ValidationContext(Collections.singletonMap("name",
                        "Длина имени поля должна быть не меньше 4 символов"), false);
            } else if (!s.matches("^[a-zA-Z0-9]+$")) {
                return new ValidationContext(Collections.singletonMap("name", "Имя поля не должно содержать пробелов"), false);
            } else {
                return new ValidationContext(Collections.emptyMap(), true);
            }
        });

        validatorMap.put("fieldSize", (Validator<Integer>) i -> {
            if (i == null || i <= 0) {
                return new ValidationContext(Collections.singletonMap("size", "Размер поля не может быть меньше 0"), false);
            } else if (i > 50) {
                return new ValidationContext(Collections.singletonMap("size", "Размер поля не может быть больше 50"), false);
            } else {
                return new ValidationContext(Collections.emptyMap(), true);
            }
        });

        return validatorMap;
    }
}
