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

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID = "X-Correlation-ID";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String correlationId = request.getHeaders().getFirst(CORRELATION_ID);
        if(correlationId ==null || correlationId.isBlank()){
            correlationId = UUID.randomUUID().toString();
        }
        MDC.put("correlationId", correlationId);
        ServerHttpRequest mutatedRequest = request.mutate().header(
                CORRELATION_ID,correlationId
        ).build();
        String method = request.getMethod().name();
        String uri =request.getURI().toString();
        String clientIp = getClientIp(request);
        String userAgent = request.getHeaders().getFirst("User-Agent");
        long requestSize = request.getHeaders().getContentLength();
        String timestamp = Instant.now().toString();
        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("correlationId", correlationId);
        logMap.put("method", method);
        logMap.put("uri", uri);
        logMap.put("clientIp", clientIp);
        logMap.put("userAgent", userAgent);
        logMap.put("timestamp", timestamp);
        logMap.put("requestSize", requestSize);
        log.info("REQUEST_LOG :: {}", logMap);
        return chain.filter(
                exchange.mutate().request(mutatedRequest).build()
        ).doFinally(signalType -> {
            MDC.remove("correlationId");
        });
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private String getClientIp(ServerHttpRequest request){
        String xForwardedFor  = request.getHeaders().getFirst("x-Forward-for");
        if(xForwardedFor!=null && !xForwardedFor.isBlank()){
            return xForwardedFor.split(",")[0];
        }
        if(request.getRemoteAddress()!=null){
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
        return "UNKNOWN";
    }
}
