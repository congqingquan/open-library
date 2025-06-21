package org.cqq.openlibrary.trade.config.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WeChat payment config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.trade.wechat-payment-config")
public class WeChatPaymentConfig {
    
    // ================================ 商户 ================================
    
    /**
     * 商户号
     */
    private String merchantId;
    
    /**
     * 商户应用 id
     */
    private String merchantAppid;
    
    /**
     * 商户应用密钥
     */
    private String merchantAppSecret;
    
    /**
     * 商户 API 证书序列号
     */
    private String merchantApiCertSerialNumber;
    
    /**
     * 商户私钥文件路径
     */
    private String merchantPrivateKeyPath;
    
    /**
     * 商户 APIv3 密钥
     */
    private String merchantApiV3Key;
}