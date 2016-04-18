package com.tanksoffline.core.services.configuration;

import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.services.Service;

import java.util.Map;

public interface ServiceLocatorConfiguration extends ServiceConfiguration<Class<? extends Service>, Service> {
    Map<Class<? extends Service>, Factory<? extends Service>> configureFactories();
}
