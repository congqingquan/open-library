package org.cqq.openlibrary.common.config.aliyun;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 阿里云短信配置
 *
 * @author Qingquan
 */
@Data
public class AliYunSMSConfig {
    
    /**
     * 访问 Key Id
     */
    @Value("${aliyun.sms.access-key-id:none}")
    private String accessKeyId;
    
    /**
     * 访问 Key 密钥
     */
    @Value("${aliyun.sms.access-key-secret:none}")
    private String accessKeySecret;
    
    /**
     * 签名名称
     */
    @Value("${aliyun.sms.sign-name:none}")
    private String signName;
}