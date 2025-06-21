package org.cqq.openlibrary.spring.util;

import lombok.AllArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Spring transaction utils
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class SpringTransactionHelper {

    private final PlatformTransactionManager transactionManager;
    
    public TransactionStatus createDefaultTransaction() {
        return createTransaction(TransactionDefinition.ISOLATION_REPEATABLE_READ, TransactionDefinition.PROPAGATION_REQUIRED);
    }

    public TransactionStatus createTransaction(int isolationLevel, int propagationBehavior) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(isolationLevel);
        def.setPropagationBehavior(propagationBehavior);
        return transactionManager.getTransaction(def);
    }

    public void commit(TransactionStatus transaction) {
        this.transactionManager.commit(transaction);
    }

    public void rollback(TransactionStatus transaction) {
        this.transactionManager.rollback(transaction);
    }
}
