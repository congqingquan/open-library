package org.cqq.openlibrary.common.util;

import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * Code utils (每秒最多9999单, 规则: "业务编码[n]yyyyMMddHHmmss[14]订单数[4]")
 *
 * @author Qingquan
 */
public class CodeUtils {
    
    // ========================================================= Static =========================================================
    
    public static final Integer MAX_PER_SECOND = 9999;
    
    private static final DateTimeFormatter GEN_CODE_SEQUENCE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * if redis.call('hsetnx', KEYS[1], 'count', 1) == 1
     * then
     *     redis.call('expire', KEYS[1], ARGV[1]);
     *     return 1;
     * else
     *     local count = tonumber(redis.call('hget', KEYS[1], 'count'));
     *     redis.call('hmset', KEYS[1], 'count', count + 1);
     *     return count + 1;
     * end
     */
    private static final String GEN_CODE_SEQUENCE_LUA =
            "if redis.call('hsetnx', KEYS[1], 'count', 1) == 1 then redis.call('expire', KEYS[1], ARGV[1]); return 1; " +
                    "else local count = tonumber(redis.call('hget', KEYS[1], 'count')); redis.call('hmset', KEYS[1], 'count', count + 1); " +
                    "return count + 1; " +
                    "end";
    
    // ========================================================= Member =========================================================
    
    private final RedissonClient redissonClient;
    
    private final String codeSequenceKeyPrefix;

    @Autowired
    public CodeUtils(RedissonClient redissonClient) {
        this(redissonClient, "GEN_CODE_SEQUENCE");
    }
    
    public CodeUtils(RedissonClient redissonClient, String codeSequenceKeyPrefix) {
        this.redissonClient = redissonClient;
        this.codeSequenceKeyPrefix = codeSequenceKeyPrefix;
    }
    
    public String generate(String type) {
        String dateTimeNow = GEN_CODE_SEQUENCE_FORMATTER.format(LocalDateTime.now());
        RScript script = redissonClient.getScript();
        Long count = script.eval(
                RScript.Mode.READ_WRITE, GEN_CODE_SEQUENCE_LUA,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(String.join("_", codeSequenceKeyPrefix, dateTimeNow)),
                1
        );
        if (count > MAX_PER_SECOND) {
            throw new RuntimeException("系统繁忙");
        }
        return String.format("%s%s%04d", type, dateTimeNow, count);
    }
}