package com.umar.booking_service.config;

import com.umar.common.ValidationConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ValidationConfig.class)
@Configuration
public class BookingValidationConfig {
}
