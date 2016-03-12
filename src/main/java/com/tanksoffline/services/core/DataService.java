package com.tanksoffline.services.core;

public interface DataService extends Service {
    <T> T save(T item);
    <T> T remove(T item);
    <T> T update(T item);
    <T> T refresh(T item);
    <T> T find(Class<T> itemClass, Object id);
    <T> T fetch(T item, String... fetchedFields);
}
