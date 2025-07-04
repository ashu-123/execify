package com.rce.execify.rateLimiting;

import com.rateLimiter.TokenBucketRateLimiter;

public final class TokenBucketApiRateLimiter implements ApiRateLimiter {

    private final TokenBucketRateLimiter tokenBucketRateLimiter;

    public TokenBucketApiRateLimiter(TokenBucketRateLimiter tokenBucketRateLimiter) {
        this.tokenBucketRateLimiter = tokenBucketRateLimiter;
    }

    public TokenBucketRateLimiter getTokenBucketRateLimiter() {
        return tokenBucketRateLimiter;
    }
}
