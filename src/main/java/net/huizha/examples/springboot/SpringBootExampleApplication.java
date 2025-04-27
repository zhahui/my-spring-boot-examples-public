package net.huizha.examples.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class SpringBootExampleApplication {

    public static void main(String[] args) {
        LOGGER.debug("Before SpringApplication.run()");
        SpringApplication.run(SpringBootExampleApplication.class, args);
    }
}
