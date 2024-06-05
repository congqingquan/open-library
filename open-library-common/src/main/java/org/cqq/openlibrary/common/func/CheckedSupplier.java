package org.cqq.openlibrary.common.func;

/**
 * Checked supplier
 *
 * @author Qingquan.Cong
 */
@FunctionalInterface
public interface CheckedSupplier<R, X extends Throwable> {

    R get() throws X;
}