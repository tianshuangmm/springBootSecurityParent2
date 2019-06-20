package com.ts.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.ts.springboot")
public class Springbootsecurity03Application {

    public static void main(String[] args) {
        SpringApplication.run(Springbootsecurity03Application.class, args);
    }

}
