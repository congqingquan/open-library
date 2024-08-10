package org.cqq.openlibrary.common.func.checked;

@FunctionalInterface
public interface CheckedPredicate<T, X extends Throwable> {

    boolean test(T t) throws X;
}