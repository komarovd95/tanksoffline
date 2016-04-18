package com.tanksoffline.core.utils.observer;

@FunctionalInterface
public interface Observer<T> {
    void observe(Observable<? extends T> observable, T oldValue, T newValue);
}
