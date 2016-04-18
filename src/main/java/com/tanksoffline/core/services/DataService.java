package com.tanksoffline.core.services;

import java.util.List;
import java.util.Map;

public interface DataService extends Service {
    <T> T save(T item);
    <T> T remove(T item);
    <T> T update(T item);
    <T> T refresh(T item);

    <T> T findById(Class<T> itemClass, Object id);
    <T> List<T> findAll(Class<T> itemClass);
    <T> List<T> findBy(Class<T> itemClass, Map<String, Object> args);
    <T> List<T> findBy(Class<T> itemClass, String paramName, Object paramValue);
    <T> List<T> findBy(String query, Object... args);

    <T> T fetch(T item, String... fetchedFields);
}
