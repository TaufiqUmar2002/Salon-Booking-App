package com.umar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class ReviewSecurityConfig extends BasicSecurityConfig {
}
