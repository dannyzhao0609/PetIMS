package com.petims;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.petims.mapper")
public class PetimsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetimsApplication.class, args);
    }
}
