package com.tanksoffline.services.core;

import com.tanksoffline.application.utils.Factory;

import java.util.Map;

public interface ServiceLocatorConfiguration {
    Map<Class<?>, Service> configureServices();
    Map<Class<?>, Factory<? extends Service>> configureFactories();
}
