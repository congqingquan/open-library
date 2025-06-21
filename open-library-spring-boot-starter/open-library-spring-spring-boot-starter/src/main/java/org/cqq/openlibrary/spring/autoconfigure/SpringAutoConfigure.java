package org.cqq.openlibrary.spring.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.spring.template.SpringTransactionTemplate;
import org.cqq.openlibrary.spring.util.SpringTransactionHelper;
import org.cqq.openlibrary.spring.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring auto configure
 *
 * @author Qingquan
 */
@Slf4j
@EnableConfigurationProperties(SpringConfig.class)
@Configuration
public class SpringAutoConfigure {
    
    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }
    
    @Bean
    public SpringTransactionHelper springTransactionHelper(@Autowired(required = false) PlatformTransactionManager platformTransactionManager) {
        if (platformTransactionManager == null) {
            return null;
        }
        return new SpringTransactionHelper(platformTransactionManager);
    }
    
    @Bean
    public SpringTransactionTemplate springTransactionTemplate(@Autowired(required = false) SpringTransactionHelper springTransactionHelper) {
        if (springTransactionHelper == null) {
            return null;
        }
        return new SpringTransactionTemplate(springTransactionHelper);
    }
}
