package com.rce.execify.rateLimiting;

import com.rateLimiter.FixedWindowRateLimiter;

public final class FixedWindowApiRateLimiter implements ApiRateLimiter {

    private final FixedWindowRateLimiter fixedWindowRateLimiter;

    public FixedWindowApiRateLimiter(FixedWindowRateLimiter fixedWindowRateLimiter) {
        System.out.println("Here");
        this.fixedWindowRateLimiter = fixedWindowRateLimiter;
    }

    public FixedWindowRateLimiter getFixedWindowRateLimiter() {
        return fixedWindowRateLimiter;
    }
}
