package com.example.signproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.signproject.mapper")
@SpringBootApplication
public class SignProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SignProjectApplication.class, args);
    }

}
