package com.savitech.fintab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FintabApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintabApplication.class, args);
    }

}
