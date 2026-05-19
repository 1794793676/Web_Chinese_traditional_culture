package com.example.backform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.backform.mapper")
public class BackformApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackformApplication.class, args);
    }

}
