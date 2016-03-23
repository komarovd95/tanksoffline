package com.tanksoffline.application.utils;

import com.tanksoffline.core.utils.Factory;
import javafx.concurrent.Task;

import java.util.concurrent.Callable;

public class TaskFactory<T> implements Factory<Task<T>> {
    private Callable<T> callable;

    public TaskFactory(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public Task<T> create() {
        return new Task<T>() {
            @Override
            protected T call() throws Exception {
                return callable.call();
            }
        };
    }
}
