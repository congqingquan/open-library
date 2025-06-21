package org.cqq.openlibrary.redis.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.redis")
public class RedisConfig {

}
