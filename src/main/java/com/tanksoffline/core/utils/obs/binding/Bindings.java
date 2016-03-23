package com.tanksoffline.core.utils.obs.binding;

import com.tanksoffline.core.utils.obs.Observable;
import com.tanksoffline.core.utils.obs.Observer;

public class Bindings {
    public static <T> ConditionBinding<T> when(Observable<Boolean> observable) {
        return new ConditionBinding<>(observable);
    }

    public static <O, B> Binding<O, B> bind(Observable<O> observable, BindFunction<O, B> function) {
        return new DefaultBinding<O, B>(observable) {
            @Override
            public void observe(Observable<O> observable, O oldValue, O newValue) {
                function.apply(observable, bound, oldValue, newValue);
            }
        };
    }

    public static <T, E> void bidirectionalBind(Observable<T> observable1, Observable<E> observable2,
                                                BindFunction<T, E> func1, BindFunction<E, T> func2) {
        new DefaultBinding<T, E>(observable1) {
            @Override
            public void observe(Observable<T> observable, T oldValue, T newValue) {
                func1.apply(observable, bound, oldValue, newValue);
            }
        };

        new DefaultBinding<E, T>(observable2) {
            @Override
            public void observe(Observable<E> observable, E oldValue, E newValue) {
                func2.apply(observable, bound, oldValue, newValue);
            }
        };
    }
}
