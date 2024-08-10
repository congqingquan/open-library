package org.cqq.openlibrary.common.func.checked;

@FunctionalInterface
public interface CheckedFunction<T, R, X extends Throwable> {

    R apply(T t) throws X;
}