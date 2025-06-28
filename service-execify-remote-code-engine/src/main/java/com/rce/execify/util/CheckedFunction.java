package com.rce.execify.util;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws Exception;

    static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> checkedFunction) {
        return (T t) -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception exception) {
                throw new RuntimeException();
            }
        };
    }
}

