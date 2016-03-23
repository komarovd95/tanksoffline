package com.tanksoffline.core.utils;

@FunctionalInterface
public interface Factory<T> {
    T create();
}
