package org.cqq.openlibrary.redis.template;

import lombok.AllArgsConstructor;
import org.cqq.openlibrary.common.func.checked.CheckedSupplier;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Redis lock template
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class RedisLockTemplate {

    private final RedissonClient redissonClient;

    /**
     * 执行
     * @param lockKey lock key
     * @param waitTime 等待时长
     * @param timeUnit 等待时长单位
     * @param lockFailedException 加锁失败时抛出的异常
     * @param task 执行体
     */
    public <R, LX extends Throwable, TX extends Throwable> R execute(String lockKey, Long waitTime, TimeUnit timeUnit, LX lockFailedException,
                                                                     CheckedSupplier<R, TX> task) throws LX, TX {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(waitTime, timeUnit)) {
                throw lockFailedException;
            }
            return task.get();
        } catch (InterruptedException e) {
            throw lockFailedException;
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}