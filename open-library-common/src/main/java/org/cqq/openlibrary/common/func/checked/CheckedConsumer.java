package org.cqq.openlibrary.common.func.checked;

@FunctionalInterface
public interface CheckedConsumer<T, X extends Throwable> {

    void accept(T t) throws X;
}