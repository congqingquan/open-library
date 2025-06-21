package org.cqq.openlibrary.notification.autoconfigure;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Notification auto configure
 *
 * @author Qingquan
 */
@Slf4j
@EnableConfigurationProperties(NotificationConfig.class)
@Configuration
public class NotificationAutoConfigure {
}
