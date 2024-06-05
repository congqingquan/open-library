package org.cqq.openlibrary.common.component.template;

import lombok.AllArgsConstructor;
import org.cqq.openlibrary.common.func.CheckedSupplier;
import org.cqq.openlibrary.common.util.spring.SpringTransactionHelper;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Spring transaction template
 *
 * @author Qingquan.Cong
 */
@AllArgsConstructor
public class SpringTransactionTemplate {

    private final SpringTransactionHelper springTransactionHelper;

    public final <R, X extends Throwable> void requiredExecute(Collection<Class<? extends Throwable>> rollbackFor,
                                                               CheckedSupplier<R, X> task) throws X {
        execute(TransactionDefinition.ISOLATION_REPEATABLE_READ, TransactionDefinition.PROPAGATION_REQUIRED, rollbackFor, task);
    }

    public final <R, X extends Throwable> void requiresNewExecute(Collection<Class<? extends Throwable>> rollbackFor,
                                                                  CheckedSupplier<R, X> task) throws X {
        execute(TransactionDefinition.ISOLATION_REPEATABLE_READ, TransactionDefinition.PROPAGATION_REQUIRES_NEW, rollbackFor, task);
    }

    /**
     * 执行
     *
     * @param isolationLevel      事务隔离级别
     * @param propagationBehavior 事务传播行为
     * @param rollbackFor         支持回滚的异常, 默认支持 {RuntimeException.class, Error.class}
     * @param task                执行体
     */
    public final <R, X extends Throwable> R execute(int isolationLevel, int propagationBehavior, Collection<Class<? extends Throwable>> rollbackFor,
                                                    CheckedSupplier<R, X> task) throws X {

        Set<Class<? extends Throwable>> rollbackForSet = new HashSet<>(rollbackFor);
        Collections.addAll(rollbackForSet, RuntimeException.class, Error.class);

        TransactionStatus transaction = springTransactionHelper.createTransaction(isolationLevel, propagationBehavior);
        R ret;
        try {
            ret = task.get();
            springTransactionHelper.commit(transaction);
        } catch (Throwable throwable) {
            for (Class<? extends Throwable> rollback : rollbackForSet) {
                if (rollback.isAssignableFrom(throwable.getClass())) {
                    springTransactionHelper.rollback(transaction);
                    break;
                }
            }
            throw throwable;
        }
        return ret;
    }
}