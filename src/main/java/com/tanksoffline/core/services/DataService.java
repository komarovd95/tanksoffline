package com.tanksoffline.core.services;

import com.tanksoffline.core.services.Service;

import java.util.List;
import java.util.Map;

public interface DataService extends Service {
    <T> T save(T item);
    <T> T remove(T item);
    <T> T update(T item);
    <T> T refresh(T item);
    <T> T find(Class<T> itemClass, Object id);
    <T> T fetch(T item, String... fetchedFields);

    <T> List<T> where(Class<T> itemClass, Map<String, Object> params);
    <T> List<T> where(Class<T> itemClass, String paramName, Object paramValue);
    <T> List<T> where(String query, Object... params);
}
