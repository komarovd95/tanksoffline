package com.tanksoffline.core.mvc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public interface ActionController<T> {
    Callable<T> onCreate(Map<String, Object> values);
    Callable<T> onFind(Map<String, Object> values);
    Callable<T> onUpdate(T t, Map<String, Object> values);
    Callable<T> onRemove(T t);

    Callable<T> onFindOne(Object id);
    Callable<List<T>> onFindAll();

    Callable<T> onConstruct();
    Callable<T> onDestroy();
}
