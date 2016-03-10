package tanks.app.models;

import tanks.utils.persistence.DomainObject;
import tanks.utils.persistence.Filterable;
import tanks.utils.persistence.ResultList;

import java.util.List;

public interface DomainModel<T extends DomainObject> extends Model, Filterable<ResultList<T>> {
    T find(Object id);
    List<T> findAll();
    T save(T t);
    T update(T t);
    T fetch(T t);
    List<T> fetch(List<T> fetchList);
}
