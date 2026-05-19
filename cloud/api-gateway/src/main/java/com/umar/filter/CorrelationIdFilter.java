package com.umar.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    private static final String HEADER_NAME = "X-Correlation-ID";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String correlationId = request.getHeaders().getFirst(HEADER_NAME);
        if (!isValidUUID(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }
        MDC.put("correlationId", correlationId);
        ServerHttpRequest mutatedRequest = request.mutate().
                                             header(HEADER_NAME, correlationId).build();
        String finalCorrelationId = correlationId;

        return chain.filter(
                exchange.mutate()
                        .request(mutatedRequest)
                        .build()
        ).doFinally(signal -> {
            MDC.remove("correlationId");
        });
    }

    @Override
    public int getOrder() {
        return 2;
    }

    private boolean isValidUUID(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            UUID.fromString(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
