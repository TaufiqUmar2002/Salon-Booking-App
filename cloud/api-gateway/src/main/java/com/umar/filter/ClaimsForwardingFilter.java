package com.umar.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class ClaimsForwardingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String userId = exchange.getAttribute("userId");
        String email = exchange.getAttribute("email");
        String role = exchange.getAttribute("role");
        String correlationId =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst("X-Correlation-ID");
        String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        String requestTime = Instant.now().toString();
        ServerHttpRequest mutatedRequest =
                exchange.getRequest().mutate()

                        // OPTIONAL METADATA
                        .header("X-User-Id", userId)
                        .header("X-User-Email", email)
                        .header("X-User-Role", role)
                        .header("X-Correlation-ID", correlationId)
                        .header("X-Forwarded-For", clientIp)
                        .header("X-Request-Time", requestTime)
                        .build();

        return chain.filter(
                exchange.mutate().request(mutatedRequest).build()
        );
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
