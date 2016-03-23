package com.tanksoffline.core.utils.obs;

@FunctionalInterface
public interface Observer<T> {
    void observe(Observable<T> observable, T oldValue, T newValue);
}
