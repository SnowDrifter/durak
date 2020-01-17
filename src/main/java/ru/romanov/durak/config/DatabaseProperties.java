package ru.romanov.durak.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class DatabaseProperties {

    @Value("#{systemProperties['db.server']}")
    private String server;
    @Value("#{systemProperties['db.database']}")
    private String database;
    @Value("#{systemProperties['db.user']}")
    private String user;
    @Value("#{systemProperties['db.password']}")
    private String password;

    @Value("#{systemProperties['db.minPoolSize'] ?: 3}")
    private int minPoolSize;
    @Value("#{systemProperties['db.maxPoolSize'] ?: 10}")
    private int maxPoolSize;
    @Value("#{systemProperties['db.connectionTimeout'] ?: 2000}")
    private int connectionTimeout;
    @Value("#{systemProperties['db.validationTimeout'] ?: 2000}")
    private int validationTimeout;

}