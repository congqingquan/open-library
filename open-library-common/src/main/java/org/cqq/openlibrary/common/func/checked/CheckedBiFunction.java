package org.cqq.openlibrary.common.func.checked;

@FunctionalInterface
public interface CheckedBiFunction<R, T1, T2, X extends Throwable> {

    R apply(T1 t1, T2 t2) throws X;
}