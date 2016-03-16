package com.tanksoffline.core.utils;

public class SingletonFactory<T> implements Factory<T> {
    protected T instance;
    protected final Factory<? extends T> localFactory;

    public SingletonFactory(Factory<? extends T> localFactory) {
        this.localFactory = localFactory;
    }

    @Override
    public T createItem() {
        synchronized (this) {
            if (instance == null) {
                instance = localFactory.createItem();
            }
        }
        return instance;
    }
}
