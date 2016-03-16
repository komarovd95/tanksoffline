package com.tanksoffline.core.services.configuration;

import com.tanksoffline.core.utils.Factory;

import java.util.Map;

@FunctionalInterface
public interface DIConfiguration {
    Map<String, Factory<?>> configure();
}
