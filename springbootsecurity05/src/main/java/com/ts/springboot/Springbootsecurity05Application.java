package com.ts.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.ts.springboot")
public class Springbootsecurity05Application {

    public static void main(String[] args) {
        SpringApplication.run(Springbootsecurity05Application.class, args);
    }

}
