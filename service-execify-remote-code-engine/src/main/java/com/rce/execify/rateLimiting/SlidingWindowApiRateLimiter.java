package com.rce.execify.rateLimiting;

import com.rateLimiter.SlidingWindowRateLimiter;

public final class SlidingWindowApiRateLimiter implements ApiRateLimiter {

    private final SlidingWindowRateLimiter slidingWindowRateLimiter;

    public SlidingWindowApiRateLimiter(SlidingWindowRateLimiter slidingWindowRateLimiter) {
        this.slidingWindowRateLimiter = slidingWindowRateLimiter;
    }

    public SlidingWindowRateLimiter getSlidingWindowRateLimiter() {
        return slidingWindowRateLimiter;
    }
}
