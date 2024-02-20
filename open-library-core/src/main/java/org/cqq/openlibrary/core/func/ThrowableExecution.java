package org.cqq.openlibrary.core.func;

/**
 * Throwable execution
 *
 * @author Qingquan.Cong
 */
@FunctionalInterface
public interface ThrowableExecution<X extends Throwable> {

    void execute() throws X;
}