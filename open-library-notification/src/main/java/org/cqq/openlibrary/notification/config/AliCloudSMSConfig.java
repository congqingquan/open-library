package org.cqq.openlibrary.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Ali cloud SMS config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.notification.ali-cloud-sms-config")
public class AliCloudSMSConfig {
    
    /**
     * 访问 Key Id
     */
    private String accessKeyId;
    
    /**
     * 访问 Key 密钥
     */
    private String accessKeySecret;
    
    /**
     * 签名名称
     */
    private String signName;
}