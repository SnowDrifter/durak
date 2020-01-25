package ru.romanov.durak.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class DatabaseProperties {

    @Value("#{environment['DB_SERVER'] ?: 'localhost'}")
    private String server;
    @Value("#{environment['DB_DATABASE'] ?: 'durak'}")
    private String database;
    @Value("#{environment['DB_USER'] ?: 'postgres'}")
    private String user;
    @Value("#{environment['DB_PASSWORD'] ?: '1234'}")
    private String password;

    @Value("#{environment['DB_MIN_POOL_SIZE'] ?: 3}")
    private int minPoolSize;
    @Value("#{environment['DB_MAX_POOL_SIZE'] ?: 10}")
    private int maxPoolSize;
    @Value("#{environment['DB_CONNECTION_TIMEOUT'] ?: 2000}")
    private int connectionTimeout;
    @Value("#{environment['DB_VALIDATION_TIMEOUT'] ?: 2000}")
    private int validationTimeout;

}
