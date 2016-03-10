package tanks.utils.persistence;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ResultList<T> implements Filterable<ResultList<T>> {
    private List<T> resultList;

    public ResultList(List<T> list) {
        this.resultList = list;
    }

    @Override
    public ResultList<T> where(Map<String, Object> params) {
        if (resultList != null && params != null) {
            Predicate<T> predicate = t -> true;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                predicate.and(t -> {
                    Object attrValue = DomainObject.getAttribute(t, entry.getKey());
                    Object paramValue = entry.getValue();
                    if (attrValue != null) {
                        return attrValue.equals(paramValue);
                    } else {
                        return paramValue == null;
                    }
                });
            }
            return new ResultList<>(resultList.stream().filter(predicate).collect(Collectors.toList()));
        }
        return null;
    }

    @Override
    public ResultList<T> where(String param, Object value) {
        if (resultList != null && param != null) {
            return new ResultList<>(resultList.stream().filter(t -> {
                Object attrValue = DomainObject.getAttribute(t, param);
                if (attrValue != null) {
                    return attrValue.equals(value);
                } else {
                    return value == null;
                }
            }).collect(Collectors.toList()));
        }
        return null;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public int size() {
        return resultList.size();
    }
}
