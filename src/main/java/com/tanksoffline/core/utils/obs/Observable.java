package com.tanksoffline.core.utils.obs;

import com.tanksoffline.core.utils.obs.binding.Binding;

public interface Observable<T> {
    T get();
    void set(T t);

    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);

    void bind(Binding<?, T> binding);
    void unbind(Binding<?, T> binding);
    void unbind();
}
