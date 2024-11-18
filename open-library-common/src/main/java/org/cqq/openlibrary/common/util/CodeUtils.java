package org.cqq.openlibrary.common.util;

import org.cqq.openlibrary.common.component.template.ExceptionTemplate;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * Code utils (每秒最多9999单, 规则: "业务编码[n]yyyyMMddHHmmss[14]订单数[4]")
 *
 * @author Qingquan
 */
public class CodeUtils {
    
    private static final Integer MAX_PER_SECOND = 9999;
    
    private static final DateTimeFormatter GEN_CODE_SEQUENCE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    // src/main/resources/script/gen-code.lua
    private static final String GEN_CODE_SEQUENCE_LUA =
            ExceptionTemplate.execute(
                    () -> {
                        URL resource = CodeUtils.class.getResource("/script/gen-code.lua");
                        if (resource == null) {
                            throw new RuntimeException("The file '/script/gen-code.lua' non-existent");
                        }
                        URI uri = resource.toURI();
                        return Files.readString(Paths.get(uri));
                    },
                    throwable -> new RuntimeException("Cannot read the gen-code.lua file", throwable)
            );
    
    public static String generate(RedissonClient redissonClient, String type) {
        String dateTimeNow = GEN_CODE_SEQUENCE_FORMATTER.format(LocalDateTime.now());
        RScript script = redissonClient.getScript();
        Long count = script.eval(
                RScript.Mode.READ_WRITE, GEN_CODE_SEQUENCE_LUA,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(String.join("_", "GEN_CODE", type, dateTimeNow)),
                1
        );
        if (count > MAX_PER_SECOND) {
            throw new RuntimeException("系统繁忙");
        }
        return String.format("%s%s%04d", type, dateTimeNow, count);
    }
}