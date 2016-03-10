package tanks.services;

import tanks.utils.persistence.ResultList;

import java.util.List;

@Provider
public interface DataService {
    <T> T save(T item);
    <T> T remove(T item);
    <T> T update(T item);
    <T> T refresh(T item);
    <T> T find(Class<T> itemClass, Object id);
    <T> T fetch(T item, String... fetchedFields);
    <T> ResultList<T> findAll(Class<T> itemClass);

    void startUp();
    void shutDown();
}
