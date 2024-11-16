package org.cqq.openlibrary.common.component.template;

import org.cqq.openlibrary.common.func.checked.CheckedSupplier;

import java.util.function.Function;

/**
 * 异常执行模板
 *
 * @author Qingquan
 */
public class ExceptionTemplate {
    
    /**
     * 执行
     *
     * @param supplier          执行体
     * @param throwableSupplier 抛出异常 Supplier
     * @param <R>               返回值类型
     * @param <X>               抛出异常类型
     * @throws X 抛出异常
     */
    public static <R, X extends Throwable> R execute(CheckedSupplier<R, Throwable> supplier,
                                                     Function<Throwable, X> throwableSupplier
    ) throws X {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            throw throwableSupplier.apply(throwable);
        }
    }
}