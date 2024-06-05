package org.cqq.openlibrary.common.func;

public interface CheckedFunction<T, R, X extends Throwable> {

    R apply(T value) throws X;
}