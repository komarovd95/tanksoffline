package com.tanksoffline.core.mvc;

import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.utils.observer.Observer;
import com.tanksoffline.core.utils.observer.Property;

public abstract class BaseModel<T> implements ActiveModel<T> {
    protected static final DataService dataService = ServiceLocator.getInstance().getService(DataService.class);

    protected Property<T> modelProperty;

    @Override
    public T save() {
        modelProperty.set(dataService.save(modelProperty.get()));
        return modelProperty.get();
    }

    @Override
    public T update() {
        modelProperty.set(dataService.update(modelProperty.get()));
        return modelProperty.get();
    }

    @Override
    public T remove() {
        modelProperty.set(dataService.remove(modelProperty.get()));
        return modelProperty.get();
    }

    @Override
    public T refresh() {
        modelProperty.set(dataService.refresh(modelProperty.get()));
        return modelProperty.get();
    }

    @Override
    public void addObserver(Observer<? super T> observer) {
        modelProperty.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer<? super T> observer) {
        modelProperty.removeObserver(observer);
    }
}
