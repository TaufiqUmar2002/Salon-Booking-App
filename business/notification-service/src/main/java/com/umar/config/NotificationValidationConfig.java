package com.umar.config;

import com.umar.common.ValidationConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ValidationConfig.class)
@Configuration
public class NotificationValidationConfig {
}
