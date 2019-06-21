package com.ts.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan("com.ts.springboot")
public class Springbootsecurity04Application {

    public static void main(String[] args) {
        SpringApplication.run(Springbootsecurity04Application.class, args);
    }

}
