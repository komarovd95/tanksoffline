package com.tanksoffline.core.utils.observer;

public interface Observable<T> {
    void addObserver(Observer<? super T> observer);
    void removeObserver(Observer<? super T> observer);
}
