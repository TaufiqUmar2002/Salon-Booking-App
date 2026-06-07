package com.umar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@ComponentScan("com.umar")
public class CommonSecurity {
    public static void main(String[] args) {
        SpringApplication.run(CommonSecurity.class);
    }
}
