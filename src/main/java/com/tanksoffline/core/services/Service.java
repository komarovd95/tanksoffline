package com.tanksoffline.core.services;

public interface Service {
    void start();
    void shutdown();

    boolean isStarted();

    default void restart() {
        shutdown();
        start();
    }
}
