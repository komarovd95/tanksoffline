package com.tanksoffline.application.configuration;

import com.tanksoffline.core.services.configuration.ServiceConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDBConfiguration implements ServiceConfiguration<String, String> {
    @Override
    public Map<String, String> configure() {
        Map<String, String> configuration = new HashMap<>();

        configuration.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.put("hibernate.connection.url", "jdbc:h2:mem:test");
        configuration.put("hibernate.driver", "org.h2.Driver");
        configuration.put("hibernate.username", "sa");
        configuration.put("hibernate.password", "");
        configuration.put("hibernate.hbm2ddl.auto", "create-drop");
        configuration.put("hibernate.show_sql", "true");

        return configuration;
    }
}
