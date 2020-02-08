package ru.romanov.durak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.romanov.durak")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
