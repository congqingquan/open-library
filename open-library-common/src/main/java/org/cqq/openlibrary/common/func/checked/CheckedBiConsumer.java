package org.cqq.openlibrary.common.func.checked;

@FunctionalInterface
public interface CheckedBiConsumer<T1, T2, X extends Throwable> {

    void accept(T1 t1, T2 t2) throws X;
}