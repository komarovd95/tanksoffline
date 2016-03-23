package com.tanksoffline.core.utils.obs.binding;

import com.tanksoffline.core.utils.obs.Observable;

public class ConditionBinding<T> extends DefaultBinding<Boolean, T> {
    private T thenValue;
    private T otherValue;

    public ConditionBinding(Observable<Boolean> observable) {
        super(observable);
    }

    private ConditionBinding(Observable<Boolean> observable, T then, T other) {
        super(observable);
        this.thenValue = then;
        this.otherValue = other;
    }

    public ConditionBinding<T> then(T value) {
        return new ConditionBinding<>(owner, value, otherValue);
    }

    public ConditionBinding<T> otherwise(T value) {
        return new ConditionBinding<>(owner, thenValue, value);
    }

    @Override
    public void observe(Observable<Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (owner == observable) {
            if (newValue) {
                bound.set(thenValue);
            } else {
                bound.set(otherValue);
            }
        }
    }
}
