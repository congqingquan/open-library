package org.cqq.openlibrary.persistence.template;

import lombok.AllArgsConstructor;
import org.cqq.openlibrary.core.func.ThrowableExecution;
import org.cqq.openlibrary.persistence.transaction.SpringTransactionHelper;
import org.springframework.transaction.TransactionDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Redis spring template
 *
 * @author Qingquan.Cong
 */
@AllArgsConstructor
public class RedisSpringTemplate {

    private final RedisLockTemplate redisLockTemplate;

    private final SpringTransactionTemplate springTransactionTemplate;

    public <LX extends Throwable, TX extends Throwable> void tryLock3sWithinDefaultTransaction(String lockKey, LX lockFailedException,
                                                                                      Collection<Class<? extends Throwable>> rollbackFor,
                                                                                      ThrowableExecution<TX> task) throws LX, TX {
        executeWithinTransaction(
                lockKey, 3L, TimeUnit.SECONDS, lockFailedException,
                TransactionDefinition.ISOLATION_REPEATABLE_READ, TransactionDefinition.PROPAGATION_REQUIRED, rollbackFor, task
        );
    }

    public <LX extends Throwable, TX extends Throwable> void tryLock3sWithinNewTransaction(String lockKey, LX lockFailedException,
                                                                                               Collection<Class<? extends Throwable>> rollbackFor,
                                                                                               ThrowableExecution<TX> task) throws LX, TX {
        executeWithinTransaction(
                lockKey, 3L, TimeUnit.SECONDS, lockFailedException,
                TransactionDefinition.ISOLATION_REPEATABLE_READ, TransactionDefinition.PROPAGATION_REQUIRES_NEW, rollbackFor, task
        );
    }

    /**
     * 事务中执行
     * @param lockKey lock key
     * @param waitTime 等待时长
     * @param timeUnit 等待时长单位
     * @param lockFailedException 加锁失败时抛出的异常
     * @param isolationLevel 事务隔离级别
     * @param propagationBehavior 事务传播行为
     * @param rollbackFor 支持回滚的异常, 默认支持 {RuntimeException.class, Error.class}
     * @param task 执行体
     */
    public <LX extends Throwable, TX extends Throwable> void executeWithinTransaction(String lockKey, Long waitTime, TimeUnit timeUnit, LX lockFailedException,
                                                                                      int isolationLevel, int propagationBehavior,
                                                                                      Collection<Class<? extends Throwable>> rollbackFor,
                                                                                      ThrowableExecution<TX> task) throws LX, TX {
        redisLockTemplate.execute(
                lockKey, waitTime, timeUnit, lockFailedException,
                () -> springTransactionTemplate.execute(isolationLevel, propagationBehavior, rollbackFor, task)
        );
    }

    public static void main(String[] args) {
        RedisSpringTemplate redisSpringTemplate = new RedisSpringTemplate(null, null);
        SpringTransactionHelper transactionHelper = new SpringTransactionHelper(null);

        redisSpringTemplate.executeWithinTransaction("lock", 3L, TimeUnit.SECONDS, new RuntimeException(""),
                TransactionDefinition.ISOLATION_REPEATABLE_READ, TransactionDefinition.PROPAGATION_REQUIRED, Arrays.asList(Exception.class), () -> {

                }
        );
    }
}