package com.petims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.petims")
public class PetimsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetimsApplication.class, args);
    }
}