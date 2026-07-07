package com.umar.booking_service.config;

import com.umar.exceptions.user.exceptionController.GlobalException;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(GlobalException.class)
@Configuration
public class BookingExceptionConfig {
    @Bean
    public GlobalException bookingException(MessageSource messageSource){
        return new GlobalException(messageSource);
    }
}
