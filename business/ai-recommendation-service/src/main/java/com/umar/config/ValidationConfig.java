package com.umar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ValidationConfig.class)
public class ValidationConfig {
}
