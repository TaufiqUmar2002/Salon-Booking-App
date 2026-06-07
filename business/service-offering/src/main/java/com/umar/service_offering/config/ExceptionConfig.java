package com.umar.service_offering.config;

import com.umar.exceptions.user.exceptionController.GlobalException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(GlobalException.class)
@Configuration
public class ExceptionConfig {
    @Bean
    public GlobalException categoryException(){
        return new GlobalException();
    }
}
