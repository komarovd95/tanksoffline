package com.tanksoffline.core.mvc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public interface ActionController<T> {
    Callable<T> create(Map<String, Object> values);
    Callable<? extends T> findBy(Map<String, Object> values);
    Callable<T> update(Map<String, Object> values);
    Callable<T> remove();

    Callable<List<? extends T>> list();

    Callable<T> construct(T t);
    Callable<T> destroy();
}
