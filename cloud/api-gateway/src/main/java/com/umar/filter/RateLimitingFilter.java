//package com.umar.filter;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//
//import org.springframework.core.Ordered;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//
//import org.springframework.stereotype.Component;
//
//import org.springframework.web.server.ServerWebExchange;
//
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//
//import java.time.Instant;
//
//import java.util.Map;
//
//@Slf4j
//@Component
//public class RateLimitingFilter
//        implements GlobalFilter, Ordered {
//
//    private final ReactiveStringRedisTemplate redisTemplate;
//
//    private final ObjectMapper objectMapper =
//            new ObjectMapper();
//
//    public RateLimitingFilter(
//            ReactiveStringRedisTemplate redisTemplate) {
//
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Override
//    public Mono<Void> filter(
//            ServerWebExchange exchange,
//            GatewayFilterChain chain) {
//
//        String path =
//                exchange.getRequest()
//                        .getURI()
//                        .getPath();
//
//        String userId =
//                exchange.getRequest()
//                        .getHeaders()
//                        .getFirst("X-User-Id");
//
//        String clientIp =
//                exchange.getRequest()
//                        .getRemoteAddress()
//                        .getAddress()
//                        .getHostAddress();
//
//        // STEP 1 — RESOLVE BUCKET
//        RateLimitConfig config =
//                resolveConfig(path, userId);
//
//        String key =
//                userId != null
//                        ? "rl:user:" + userId
//                        : "rl:ip:" + clientIp;
//
//        return redisTemplate.opsForValue()
//                .get(key)
//
//                .flatMap(value -> {
//
//                    BucketState state;
//
//                    try {
//
//                        state =
//                                objectMapper.readValue(
//                                        value,
//                                        BucketState.class
//                                );
//
//                    } catch (Exception ex) {
//
//                        state = createNewBucket(config);
//                    }
//
//                    // STEP 2 — REFILL TOKENS
//                    long now =
//                            Instant.now()
//                                    .getEpochSecond();
//
//                    long elapsed =
//                            now - state.getLastRefill();
//
//                    double tokensToAdd =
//                            elapsed
//                                    * config.getReplenishRate();
//
//                    double newCount =
//                            Math.min(
//
//                                    state.getTokens()
//                                            + tokensToAdd,
//
//                                    config.getBurstCapacity()
//                            );
//
//                    // STEP 3 — CHECK TOKEN
//                    if (newCount < 1) {
//
//                        return rejectRequest(
//                                exchange,
//                                config
//                        );
//                    }
//
//                    // STEP 4 — CONSUME TOKEN
//                    state.setTokens(newCount - 1);
//
//                    state.setLastRefill(now);
//
//                    try {
//
//                        String json =
//                                objectMapper
//                                        .writeValueAsString(
//                                                state
//                                        );
//
//                        return redisTemplate
//                                .opsForValue()
//
//                                .set(
//                                        key,
//                                        json,
//                                        Duration.ofHours(1)
//                                )
//
//                                .then(
//                                        continueChain(
//                                                exchange,
//                                                chain,
//                                                config,
//                                                state
//                                        )
//                                );
//
//                    } catch (Exception ex) {
//
//                        return continueChain(
//                                exchange,
//                                chain,
//                                config,
//                                state
//                        );
//                    }
//
//                })
//
//                // STEP 5 — NEW BUCKET
//                .switchIfEmpty(
//
//                        createBucket(
//                                exchange,
//                                chain,
//                                key,
//                                config
//                        )
//                )
//
//                // STEP 6 — FAIL OPEN
//                .onErrorResume(ex -> {
//
//                    log.error(
//                            "Redis unavailable",
//                            ex
//                    );
//
//                    return chain.filter(exchange);
//                });
//    }
//
//    @Override
//    public int getOrder() {
//
//        return 5;
//    }
//
//    private Mono<Void> continueChain(
//            ServerWebExchange exchange,
//            GatewayFilterChain chain,
//            RateLimitConfig config,
//            BucketState state) {
//
//        exchange.getResponse()
//                .getHeaders()
//                .add(
//                        "X-RateLimit-Remaining",
//                        String.valueOf(
//                                (int) state.getTokens()
//                        )
//                );
//
//        exchange.getResponse()
//                .getHeaders()
//                .add(
//                        "X-RateLimit-Replenish-Rate",
//                        String.valueOf(
//                                config.getReplenishRate()
//                        )
//                );
//
//        exchange.getResponse()
//                .getHeaders()
//                .add(
//                        "X-RateLimit-Burst-Capacity",
//                        String.valueOf(
//                                config.getBurstCapacity()
//                        )
//                );
//
//        return chain.filter(exchange);
//    }
//
//    private Mono<Void> rejectRequest(
//            ServerWebExchange exchange,
//            RateLimitConfig config) {
//
//        exchange.getResponse()
//                .setStatusCode(
//                        HttpStatus.TOO_MANY_REQUESTS
//                );
//
//        exchange.getResponse()
//                .getHeaders()
//                .setContentType(
//                        MediaType.APPLICATION_JSON
//                );
//
//        long retryAfter =
//                Math.max(
//                        1,
//                        1 / config.getReplenishRate()
//                );
//
//        exchange.getResponse()
//                .getHeaders()
//                .add(
//                        "Retry-After",
//                        String.valueOf(retryAfter)
//                );
//
//        byte[] body =
//                """
//                {
//                  "error":"Too many requests",
//                  "retryAfter":3
//                }
//                """.getBytes();
//
//        return exchange.getResponse()
//                .writeWith(
//
//                        Mono.just(
//
//                                exchange.getResponse()
//                                        .bufferFactory()
//                                        .wrap(body)
//                        )
//                );
//    }
//
//    private Mono<Void> createBucket(
//            ServerWebExchange exchange,
//            GatewayFilterChain chain,
//            String key,
//            RateLimitConfig config) {
//
//        BucketState state =
//                createNewBucket(config);
//
//        try {
//
//            String json =
//                    objectMapper
//                            .writeValueAsString(state);
//
//            return redisTemplate.opsForValue()
//
//                    .set(
//                            key,
//                            json,
//                            Duration.ofHours(1)
//                    )
//
//                    .then(
//                            continueChain(
//                                    exchange,
//                                    chain,
//                                    config,
//                                    state
//                            )
//                    );
//
//        } catch (Exception ex) {
//
//            return chain.filter(exchange);
//        }
//    }
//
//    private BucketState createNewBucket(
//            RateLimitConfig config) {
//
//        return new BucketState(
//
//                config.getBurstCapacity(),
//
//                Instant.now()
//                        .getEpochSecond()
//        );
//    }
//
//    private RateLimitConfig resolveConfig(
//            String path,
//            String userId) {
//
//        // LOGIN LIMIT
//        if (path.equals("/api/users/login")) {
//
//            return new RateLimitConfig(
//                    5.0 / 900.0,
//                    5
//            );
//        }
//
//        // PAYMENT LIMIT
//        if (path.startsWith("/api/payments")) {
//
//            return new RateLimitConfig(
//                    5.0 / 60.0,
//                    5
//            );
//        }
//
//        // BOOKING LIMIT
//        if (path.startsWith("/api/bookings")) {
//
//            return new RateLimitConfig(
//                    10.0 / 60.0,
//                    15
//            );
//        }
//
//        // PUBLIC ROUTES
//        if (userId == null) {
//
//            return new RateLimitConfig(
//                    5,
//                    10
//            );
//        }
//
//        // DEFAULT AUTHENTICATED
//        return new RateLimitConfig(
//                20,
//                40
//        );
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    static class BucketState {
//
//        private double tokens;
//
//        private long lastRefill;
//    }
//
//    @Data
//    @AllArgsConstructor
//    static class RateLimitConfig {
//
//        private double replenishRate;
//
//        private int burstCapacity;
//    }
//}
