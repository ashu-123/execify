package com.rce.execify.rateLimiting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class CustomRateLimitingFilter implements WebFilter {

    private final ApiRateLimiter apiRateLimiter;

    private final ObjectMapper objectMapper;

    public CustomRateLimitingFilter(ApiRateLimiter apiRateLimiter, ObjectMapper objectMapper) {
        this.apiRateLimiter = apiRateLimiter;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Apply filter only to specific path(s)
        if (path.startsWith("/code/")) {
            var req = exchange.getRequest();
            var res = exchange.getResponse();

            String clientIp = req.getRemoteAddress().toString();
            String clientId = "rate-limit: " + clientIp;

            boolean isAllowed = switch (apiRateLimiter) {
                case FixedWindowApiRateLimiter fixedWindowApiRateLimiter ->
                        fixedWindowApiRateLimiter.getFixedWindowRateLimiter().isAllowed(clientId);
                case SlidingWindowApiRateLimiter slidingWindowApiRateLimiter ->
                        slidingWindowApiRateLimiter.getSlidingWindowRateLimiter().isAllowed(clientId);
                case TokenBucketApiRateLimiter tokenBucketApiRateLimiter ->
                        tokenBucketApiRateLimiter.getTokenBucketRateLimiter().isAllowed(clientId);
            };
            if (isAllowed) {
                System.out.println(isAllowed);
            } else {
                res.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

                var headers = res.getHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-Rate-Limit-Limit", "10");
                headers.set("X-Rate-Limit-Remaining", "0");
                headers.set("Retry-After", "60"); // In seconds

                String errorBody = """
                        {
                          "status": 429,
                          "error": "Too Many Requests",
                          "message": "Rate limit exceeded. Try again later."
                          "timestamp": %d,
                          "path" : %s,
                        }
                        """.formatted(System.currentTimeMillis(), req.getURI());

                DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
                DataBuffer buffer = bufferFactory.wrap(errorBody.getBytes(StandardCharsets.UTF_8));

                return res.writeWith(Mono.just(buffer));
            }
        }

        // Continue the filter chain
        return chain.filter(exchange);
    }
}

