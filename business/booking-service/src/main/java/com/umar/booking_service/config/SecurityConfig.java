package com.umar.booking_service.config;

import com.umar.config.BasicSecurityConfig;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class SecurityConfig extends BasicSecurityConfig {

}
