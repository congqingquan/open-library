package org.cqq.openlibrary.spring.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.spring")
public class SpringConfig {

}
