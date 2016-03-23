package com.tanksoffline.core.utils.obs.binding;

import com.tanksoffline.core.utils.obs.Observable;
import com.tanksoffline.core.utils.obs.Observer;

public interface Binding<O, B> extends Observer<O> {
    Observable<O> getOwner();
    Observable<B> getBound();
    void addBound(Observable<B> bound);
    void removeBound(Observable<B> bound);
}
