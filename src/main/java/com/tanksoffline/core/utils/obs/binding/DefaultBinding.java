package com.tanksoffline.core.utils.obs.binding;

import com.tanksoffline.core.utils.obs.Observable;

public abstract class DefaultBinding<O, B> implements Binding<O, B> {
    protected Observable<O> owner;
    protected Observable<B> bound;

    public DefaultBinding(Observable<O> owner) {
        this.owner = owner;
        owner.addObserver(this);
    }

    @Override
    public Observable<O> getOwner() {
        return owner;
    }

    @Override
    public Observable<B> getBound() {
        return bound;
    }

    @Override
    public void addBound(Observable<B> observable) {
        this.bound = observable;
    }

    @Override
    public void removeBound(Observable<B> observable) {
        if (bound == observable) {
            bound = null;
        }
    }
}
