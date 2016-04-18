package com.tanksoffline.core.utils.observer;

public interface Property<T> extends Observable<T> {
    T get();
    void set(T t);
}
