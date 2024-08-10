package org.cqq.openlibrary.common.func.checked;

@FunctionalInterface
public interface CheckedBiPredicate<T1, T2, X extends Throwable> {

    boolean test(T1 t1, T2 t2) throws X;
}