package org.cqq.openlibrary.common.func;

/**
 * Throwable execution
 *
 * @author Qingquan.Cong
 */
@FunctionalInterface
public interface ThrowableExecution<X extends Throwable> {

    void execute() throws X;
}