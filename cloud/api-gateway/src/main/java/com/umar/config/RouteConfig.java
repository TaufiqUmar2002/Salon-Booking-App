//package com.umar.config;
//
//import com.umar.filter.JwtAuthFilter;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RouteConfig {
//
//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder, JwtAuthFilter filter) {
//
//        return builder.routes()
//
//                .route("user-service", r -> r.path("/auth/**")
//                        .uri("http://localhost:8081"))
//
//                .route("salon-service", r -> r.path("/salons/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("http://localhost:8082"))
//
//                .build();
//    }
//}
