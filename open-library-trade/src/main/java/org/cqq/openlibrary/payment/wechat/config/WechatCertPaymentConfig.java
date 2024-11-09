package org.cqq.openlibrary.payment.wechat.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 微信证书支付配置
 *
 * @author Qingquan
 */
@Data
public class WechatCertPaymentConfig {
    
    // ================================ 商户 ================================
    
    /**
     * 商户号
     */
    @Value("${payment.wechat.merchant.id}")
    private String merchantId;
    
    /**
     * 商户应用 id
     */
    @Value("${payment.wechat.merchant.appid}")
    private String merchantAppid;
    
    /**
     * 商户应用密钥
     */
    @Value("${payment.wechat.merchant.app-secret}")
    private String merchantAppSecret;
    
    /**
     * 商户 API 证书序列号
     */
    @Value("${payment.wechat.merchant.api-cert-serial-number}")
    private String merchantApiCertSerialNumber;
    
    /**
     * 商户私钥文件路径
     */
    @Value("${payment.wechat.merchant.private-key-path}")
    private String merchantPrivateKeyPath;
    
    /**
     * 商户 APIv3 密钥
     */
    @Value("${payment.wechat.merchant.api-v3-key}")
    private String merchantApiV3Key;
    
    /**
     * 创建自动更新平台证书的配置类(不同商户全局只应初始化一次)
     */
    public RSAAutoCertificateConfig createRsaAutoCertConfig() {
        return new RSAAutoCertificateConfig
                .Builder()
                .merchantId(merchantId)
                .merchantSerialNumber(merchantApiCertSerialNumber)
                .privateKeyFromPath(merchantPrivateKeyPath)
                .apiV3Key(merchantApiV3Key)
                .build();
    }
}