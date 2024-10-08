package org.cqq.openlibrary.common.func.ternary;

@FunctionalInterface
public interface TernaryFunction<R, T1, T2, T3> {

    R apply(T1 t1, T2 t2, T3 t3);
}