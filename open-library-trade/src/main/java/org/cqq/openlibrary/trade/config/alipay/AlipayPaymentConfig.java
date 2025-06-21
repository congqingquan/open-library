package org.cqq.openlibrary.trade.config.alipay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Alipay payment config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.trade.alipay-payment-config")
public class AlipayPaymentConfig {
    
    // ================================ 商户 ================================
    
    /**
     * 商户应用 id
     */
    private String merchantAppId;
    
    /**
     * 商户私钥 (非路径)
     */
    private String merchantAppPrivateKey;
    
    /**
     * 商户公钥证书路径
     */
    private String merchantAppCertPath;
    
    // ================================ 支付宝 ================================
    
    /**
     * 支付宝网关地址
     */
    private String serverUrl;
    
    /**
     * 支付宝公钥证书路径
     */
    private String publicCertPath;
    
    /**
     * 支付宝根证书路径
     */
    private String rootCertPath;
}