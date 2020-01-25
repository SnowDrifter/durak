package ru.romanov.durak.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class RedisProperties {

    @Value("#{environment['REDIS_HOST'] ?: 'localhost'}")
    private String host;
    @Value("#{environment['REDIS_PORT'] ?: 6379}")
    private int port;

    @Value("#{environment['REDIS_PASSWORD'] ?: null}")
    private String password;

    @Value("#{environment['REDIS_MAX_IDLE'] ?: 3}")
    private int maxIdle;
    @Value("#{environment['REDIS_MAX_TOTAL'] ?: 10}")
    private int maxTotal;

}
