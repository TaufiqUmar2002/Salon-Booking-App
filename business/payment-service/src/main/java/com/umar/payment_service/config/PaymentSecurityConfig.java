package com.umar.payment_service.config;

import com.umar.config.BasicSecurityConfig;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class PaymentSecurityConfig extends BasicSecurityConfig {
}
