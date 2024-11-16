package org.cqq.openlibrary.common.func.checked;

@FunctionalInterface
public interface CheckedRunnable<X extends Throwable> {

    void run() throws X;
}