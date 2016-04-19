package com.tanksoffline.core.mvc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public interface ActionController<T> {
    Callable<T> onCreate(Map<String, Object> values);
    Callable<T> onFind(Map<String, Object> values);
    Callable<T> onUpdate(Map<String, Object> values);
    Callable<T> onRemove();

    Callable<T> onFindOne(Object id);
    Callable<List<T>> onFindAll();

    Callable<T> onConstruct();
    Callable<T> onDestroy();
}
