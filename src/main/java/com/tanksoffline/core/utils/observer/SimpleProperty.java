package com.tanksoffline.core.utils.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleProperty<T> implements Property<T> {
    private T item;
    private List<Observer<? super T>> observers;

    public SimpleProperty() {
        observers = new CopyOnWriteArrayList<>();
    }

    public SimpleProperty(T initialValue) {
        this();
        set(initialValue);
    }

    @Override
    public T get() {
        return item;
    }

    @Override
    public void set(T t) {
        fireUpdate(t);
        this.item = t;
    }

    private void fireUpdate(T t) {
        for (Observer<? super T> obs : observers) {
            obs.observe(this, item, t);
        }
    }

    @Override
    public void addObserver(Observer<? super T> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<? super T> observer) {
        this.observers.remove(observer);
    }
}
