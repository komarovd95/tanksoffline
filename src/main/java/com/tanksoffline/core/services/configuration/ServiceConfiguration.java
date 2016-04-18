package com.tanksoffline.core.services.configuration;

import java.util.Map;

@FunctionalInterface
public interface ServiceConfiguration<K, V> {
    Map<K, V> configure();
}
