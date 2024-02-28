package org.cqq.openlibrary.common.func;

public interface CheckedFunction<T, R> {

    R apply(T value) throws Throwable;
}