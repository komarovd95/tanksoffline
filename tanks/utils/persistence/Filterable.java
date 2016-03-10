package tanks.utils.persistence;

import java.util.Map;
import java.util.function.Predicate;

public interface Filterable<T> {
    T where(Map<String, Object> params);
    T where(String param, Object value);
}
