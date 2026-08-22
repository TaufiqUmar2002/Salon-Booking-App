package com.umar;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class AIAssistantServiceApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(AIAssistantServiceApplication.class, args);
    }

}
