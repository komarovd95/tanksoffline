package com.tanksoffline.core.services;

public interface Service {
    void start();
    void shutdown();

    default void restart() {
        shutdown();
        start();
    }
}
