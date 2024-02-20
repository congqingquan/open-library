package org.cqq.openlibrary.persistence.template;

import lombok.AllArgsConstructor;
import org.cqq.openlibrary.core.func.ThrowableExecution;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Spring transaction utils
 *
 * @author Qingquan.Cong
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
    public <LX extends Throwable, TX extends Throwable> void execute(String lockKey, Long waitTime, TimeUnit timeUnit, LX lockFailedException,
                                                                     ThrowableExecution<TX> task) throws LX, TX {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(waitTime, timeUnit)) {
                throw lockFailedException;
            }
            task.execute();
        } catch (InterruptedException e) {
            throw lockFailedException;
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
