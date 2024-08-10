package org.cqq.openlibrary.common.func.ternary;

import org.cqq.openlibrary.common.domain.R;

@FunctionalInterface
public interface TernaryPredicate<T1, T2, T3> {

    boolean test(T1 t1, T2 t2, T3 t3);
}