package com.rce.execify.rateLimiting;

public sealed interface ApiRateLimiter permits FixedWindowApiRateLimiter, SlidingWindowApiRateLimiter, TokenBucketApiRateLimiter {
}
