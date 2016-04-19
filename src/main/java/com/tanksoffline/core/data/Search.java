package com.tanksoffline.core.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Search<T> {
    T findOne(Object id);
    List<? extends T> findAll();
    List<? extends T> findBy(String query, Object... args);
    List<? extends T> findBy(Map<String, Object> ags);
    default List<? extends T> findBy(String arg, String value) {
        return findBy(Collections.singletonMap(arg, value));
    }
}
