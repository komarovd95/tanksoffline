package com.tanksoffline.core.utils.obs.binding;

import com.tanksoffline.core.utils.obs.Observable;

@FunctionalInterface
public interface BindFunction<O, B> {
    void apply(Observable<O> observable, Observable<B> bound, O oldValue, O newValue);
}
