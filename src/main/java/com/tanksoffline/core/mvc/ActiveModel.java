package com.tanksoffline.core.mvc;

import com.tanksoffline.core.utils.observer.Observable;

public interface ActiveModel<T> extends Observable<T> {
    T save();
    T update();
    T remove();
    T refresh();
}
