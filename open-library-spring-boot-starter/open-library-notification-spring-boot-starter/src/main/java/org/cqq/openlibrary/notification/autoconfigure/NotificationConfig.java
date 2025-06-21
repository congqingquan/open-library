package org.cqq.openlibrary.notification.autoconfigure;

import lombok.Data;
import org.cqq.openlibrary.notification.config.AliCloudSMSConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Notification config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.notification")
public class NotificationConfig {
    
    private AliCloudSMSConfig aliCloudSMSConfig;
}
