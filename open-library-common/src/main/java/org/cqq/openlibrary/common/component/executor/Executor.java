package org.cqq.openlibrary.common.component.executor;

/**
 * Executor
 *
 * @author Qingquan
 */
@FunctionalInterface
public interface Executor {

    Object execute(Object... args) throws Throwable;

    default boolean match(Object obj) {
        return this.getClass().isAssignableFrom(obj.getClass());
    }
}