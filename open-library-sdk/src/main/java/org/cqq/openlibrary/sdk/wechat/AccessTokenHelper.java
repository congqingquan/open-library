package org.cqq.openlibrary.sdk.wechat;

import org.cqq.openlibrary.common.util.StringUtils;
import org.cqq.openlibrary.sdk.wechat.exception.WechatSDKException;
import org.cqq.openlibrary.sdk.wechat.request.GetAccessTokenRequest;
import org.cqq.openlibrary.sdk.wechat.response.GetAccessTokenResponse;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Access token 工具类
 *
 * @author Qingquan
 */
public interface AccessTokenHelper {
    
    static String getCacheableAccessToken(GetAccessTokenRequest request,
                                          RedissonClient redissonClient) {
        return getCacheableAccessToken(
                request,
                "KEY_WECHAT_ACCESS_TOKEN",
                redissonClient,
                "LOCK_WECHAT_ACCESS_TOKEN",
                3L,
                TimeUnit.SECONDS,
                new WechatSDKException("System busy")
        );
    }
    
    static <LX extends Throwable> String getCacheableAccessToken(GetAccessTokenRequest request,
                                                                 String accessTokenKey,
                                                                 RedissonClient redissonClient,
                                                                 String lockKey,
                                                                 Long waitTime,
                                                                 TimeUnit timeUnit,
                                                                 LX lockFailedException) throws LX {
        
        RBucket<String> accessTokenBucket = redissonClient.getBucket(accessTokenKey);
        String accessToken = accessTokenBucket.get();
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(waitTime, timeUnit)) {
                throw lockFailedException;
            }
            
            // dcl
            String getAgainAccessToken = accessTokenBucket.get();
            if (StringUtils.isNotBlank(getAgainAccessToken)) {
                return getAgainAccessToken;
            }
            
            // reset token
            GetAccessTokenResponse accessTokenResponse = WechatApiSDK.execute(request);
            accessTokenBucket.set(
                    accessTokenResponse.getAccessToken(),
                    Duration.of(Math.max(accessTokenResponse.getExpiresIn() - 3, 1), ChronoUnit.SECONDS)
            );
            return accessTokenResponse.getAccessToken();
        } catch (InterruptedException e) {
            throw lockFailedException;
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
