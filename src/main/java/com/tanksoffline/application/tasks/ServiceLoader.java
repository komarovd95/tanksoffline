package com.tanksoffline.application.tasks;
import com.tanksoffline.core.services.ServiceLocator;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServiceLoader extends Service<Void> {
    private Class<? extends com.tanksoffline.core.services.Service>[] loadedService;

    @SuppressWarnings("unchecked")
    public ServiceLoader(Class... loadedService) {
        this.loadedService = loadedService;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Class<? extends com.tanksoffline.core.services.Service> c : loadedService) {
                    ServiceLocator.getInstance().getService(c).start();

                }
                return null;
            }
        };
    }
}
