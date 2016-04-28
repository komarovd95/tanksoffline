package com.tanksoffline.application.configuration;

import com.tanksoffline.core.services.configuration.ServiceConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDBConfiguration implements ServiceConfiguration<String, String> {
    @Override
    public Map<String, String> configure() {
        Map<String, String> configuration = new HashMap<>();

//        configuration.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        configuration.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/tanks");
//        configuration.put("hibernate.connection.driver_class", "org.postgresql.Driver");
//        configuration.put("hibernate.connection.username", "postgres");
//        configuration.put("hibernate.connection.password", "password");
//        configuration.put("hibernate.hbm2ddl.auto", "update");
//        configuration.put("hibernate.show_sql", "true");

        configuration.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.put("hibernate.connection.url", "jdbc:h2:./tanks");
        configuration.put("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.put("hibernate.connection.username", "dmitriy");
        configuration.put("hibernate.connection.password", "atdhfkm11");
        configuration.put("hibernate.hbm2ddl.auto", "update");
        configuration.put("hibernate.show_sql", "true");

        return configuration;
    }
}
