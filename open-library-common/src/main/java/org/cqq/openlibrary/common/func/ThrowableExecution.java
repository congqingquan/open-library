package org.cqq.openlibrary.common.func;

/**
 * Throwable execution
 *
 * @author Qingquan.Cong
 */
@FunctionalInterface
public interface ThrowableExecution<R, X extends Throwable> {

    R execute() throws X;
}