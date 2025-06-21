package org.cqq.openlibrary.redis.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.redis.template.RedisLockSpringTransactionTemplate;
import org.cqq.openlibrary.redis.template.RedisLockTemplate;
import org.cqq.openlibrary.redis.util.CodeUtils;
import org.cqq.openlibrary.spring.template.SpringTransactionTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Redis auto configure
 *
 * @author Qingquan
 */
@Slf4j
@EnableConfigurationProperties(RedisConfig.class)
@Configuration
public class RedisAutoConfigure {
    
    @Component
    public static class CodeUtilsInitializer {
        @Autowired
        public void init(@Autowired(required = false) RedissonClient redissonClient) {
            CodeUtils.init(redissonClient);
        }
    }
    
    @Bean
    public RedisLockTemplate redisLockTemplate(@Autowired(required = false) RedissonClient redissonClient) {
        if (redissonClient == null) {
            return null;
        }
        return new RedisLockTemplate(redissonClient);
    }
    
    @Bean
    public RedisLockSpringTransactionTemplate redisLockSpringTransactionTemplate(@Autowired(required = false) RedisLockTemplate redisLockTemplate,
                                                                                 @Autowired(required = false) SpringTransactionTemplate springTransactionTemplate) {
        if (redisLockTemplate == null || springTransactionTemplate == null) {
            return null;
        }
        return new RedisLockSpringTransactionTemplate(redisLockTemplate, springTransactionTemplate);
    }
}
