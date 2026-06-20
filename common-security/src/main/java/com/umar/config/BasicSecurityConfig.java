package com.umar.config;

import com.umar.filter.JwtAuthenticationFilter;
import com.umar.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class BasicSecurityConfig {

    @ConditionalOnMissingBean(name = "jwtUtil")
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }




    @ConditionalOnMissingBean(name = "filterChain")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session->session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/api/booking")
                        .permitAll()

                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @ConditionalOnMissingBean(name = "jwtAuthenticationFilter")
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        // Pass the properly managed, post-constructed jwtUtil instance
        return new JwtAuthenticationFilter(jwtUtil());
    }




}
