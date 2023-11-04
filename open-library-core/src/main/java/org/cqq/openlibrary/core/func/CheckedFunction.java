package org.cqq.openlibrary.core.func;

public interface CheckedFunction<T, R> {

    R apply(T value) throws Throwable;
}