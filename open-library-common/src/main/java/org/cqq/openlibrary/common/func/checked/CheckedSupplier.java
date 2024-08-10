package org.cqq.openlibrary.common.func.checked;

@FunctionalInterface
public interface CheckedSupplier<R, X extends Throwable> {

    R get() throws X;
}