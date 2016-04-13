package com.tanksoffline.application.tasks;

import com.tanksoffline.application.utils.TaskFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.Callable;

public class ResultSetFinder<T> extends Service<List<T>> {
    private Callable<List<T>> listCallable;

    public ResultSetFinder(Callable<List<T>> listCallable) {
        this.listCallable = listCallable;
    }

    @Override
    protected Task<List<T>> createTask() {
        return new TaskFactory<>(listCallable).create();
    }
}
