package com.tanksoffline.application.models.core;

import java.util.List;
import java.util.Map;

public interface DataModel<T> {
    T findOne(Object value);
    List<T> findAll();
    List<T> findBy(Map<String, Object> params);

    T update(T t, Map<String, Object> values);
    void delete(T t);
}
