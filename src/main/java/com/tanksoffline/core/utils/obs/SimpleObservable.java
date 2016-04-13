package com.tanksoffline.core.utils.obs;

import com.tanksoffline.core.utils.obs.binding.Binding;

import java.util.ArrayList;
import java.util.List;

public class SimpleObservable<T> implements Observable<T> {
    private T item;
    private List<Observer<T>> observers;

    public SimpleObservable() {
        observers = new ArrayList<>();
    }

    public SimpleObservable(T initialValue) {
        this();
        set(initialValue);
    }

    @Override
    public T get() {
        return item;
    }

    @Override
    public void set(T t) {
        if (item != t) {
            fireUpdate(t);
            this.item = t;
        }
    }

    private void fireUpdate(T t) {
        for (Observer<T> obs : observers) {
            obs.observe(this, item, t);
        }
    }

    @Override
    public void addObserver(Observer<T> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void bind(Binding<?, T> binding) {
        binding.addBound(this);
    }

    @Override
    public void unbind(Binding<?, T> binding) {
        binding.removeBound(this);
    }

    @Override
    public void unbind() {
        observers.stream().filter(observer -> observer.getClass().isAssignableFrom(Binding.class)).forEach(observer -> {
            observers.remove(observer);
            ((Binding<?, T>) observer).removeBound(this);
        });
    }
}
