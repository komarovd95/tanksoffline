package com.tanksoffline.core.validation;

@FunctionalInterface
public interface Validator<T> {
    ValidationContext validate(T t);
}
