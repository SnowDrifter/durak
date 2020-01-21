package ru.romanov.durak.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class RedisProperties {

    @Value("#{systemProperties['redis.host'] ?: 'localhost'}")
    private String host;
    @Value("#{systemProperties['redis.port'] ?: 6379}")
    private int port;

    @Value("#{systemProperties['redis.password'] ?: null}")
    private String password;

    @Value("#{systemProperties['redis.maxIdle'] ?: 3}")
    private int maxIdle;
    @Value("#{systemProperties['redis.maxTotal'] ?: 10}")
    private int maxTotal;

}
