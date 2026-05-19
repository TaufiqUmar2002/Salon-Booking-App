package com.umar.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.umar.util.JwtConstant;
import com.umar.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JwtUtil jwtUtil;

    private String jwtSecret= JwtConstant.SECRET_KEY;

    // PUBLIC ROUTES
    private static final List<String> PUBLIC_PATHS =
            List.of(
                    "/api/users/register",
                    "/api/users/login",
                    "/api/users/refresh-token",
                    "/api/users/forgot-password",
                    "/api/users/reset-password",
                    "/api/users/verify-email",
                    "/actuator/health"
            );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        if (isPublicRoute(path, method)) {
            return chain.filter(exchange);
        }

        // STEP 2 — READ AUTH HEADER
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isBlank()) {
            return unauthorized(exchange, "MISSING_TOKEN", "Authentication required");
        }
        // STEP 3 — VALIDATE FORMAT
        if (!authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "INVALID_FORMAT", "Invalid Authorization header format");
        }
        String token = authHeader.substring(7);
        if (token.isBlank()) {
            return unauthorized(exchange, "INVALID_FORMAT", "Token missing");
        }

        try {
            String authToken = authHeader.substring(7);
            if (!jwtUtil.validateToken(authToken)) {
                unauthorized(exchange,
                        "INVALID_TOKEN",
                        "Token is invalid or expired");
            }
            SecretKey key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            Jws<Claims> jws = Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(token);
            Claims claims =
                    jws.getPayload();

            if (claims.getExpiration() == null || claims.getExpiration().toInstant().isBefore(Instant.now())) {
                return unauthorized(
                        exchange,
                        "TOKEN_EXPIRED",
                        "Token expired. Please refresh your token.");
            }

            // STEP 6 — VALIDATE CLAIMS
            String userId = claims.getSubject();
            String role = claims.get("role", String.class);
            String email = claims.get("email", String.class);

            if (userId == null || role == null || email == null) {
                return unauthorized(
                        exchange,
                        "MISSING_CLAIMS",
                        "Invalid token claims"
                );
            }

            // STEP 7 — VALIDATE IAT
            if (claims.getIssuedAt() != null) {
                long issuedAt = claims.getIssuedAt().getTime();
                long now = System.currentTimeMillis();
                long skew = 30_000;
                if (issuedAt > now + skew) {
                    return unauthorized(exchange,
                            "INVALID_IAT",
                            "Invalid issued-at timestamp");
                }
            }

            // STEP 8 — STORE CLAIMS
            exchange.getAttributes().put("userId", userId);
            exchange.getAttributes().put("role", role);
            exchange.getAttributes().put("email", email);

            // IMPORTANT:
            // DO NOT REMOVE AUTHORIZATION HEADER

            // Gateway automatically forwards it

            ServerHttpRequest mutatedRequest =
                    exchange.getRequest().mutate()
                            .header("X-User-Id", userId)
                            .header("X-User-Role", role)
                            .build();

            // STEP 8 — CONTINUE CHAIN
            return chain.filter(exchange.mutate()
                            .request(mutatedRequest)
                            .build()
            );

        } catch (JwtException ex) {
            return unauthorized(
                    exchange,
                    "INVALID_SIGNATURE",
                    "Invalid token"
            );

        } catch (Exception ex) {
            return unauthorized(
                    exchange,
                    "MALFORMED_TOKEN",
                    "Malformed token"
            );
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }

    private boolean isPublicRoute(String path, String method) {
            if (path.startsWith("/auth/")||path.equals("/api/categories")
                    || path.startsWith("/api/services/salon/")
                    || path.equals("/api/salons")
                    || path.matches("/api/salons/\\d+")
                    || path.startsWith("/api/reviews/salon/")) {
                return true;
        }

        return PUBLIC_PATHS.contains(path);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String code, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse()
                .getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);
        try {
            byte[] body = objectMapper.writeValueAsBytes(Map.of(
                                    "errorCode", code,
                                    "error", message));
            return exchange.getResponse()
                    .writeWith(
                            Mono.just(
                                    exchange.getResponse()
                                            .bufferFactory()
                                            .wrap(body)
                            )
                    );

        } catch (Exception ex) {
            return exchange.getResponse().setComplete();
        }
    }
}
