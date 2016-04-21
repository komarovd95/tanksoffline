package com.tanksoffline.core.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Search<T> {
    T findOne(Object id);
    List<T> findAll();
    List<T> findBy(String query, Object... args);
    List<T> findBy(Map<String, Object> args);
    default List<T> findBy(String arg, String value) {
        return findBy(Collections.singletonMap(arg, value));
    }
}
