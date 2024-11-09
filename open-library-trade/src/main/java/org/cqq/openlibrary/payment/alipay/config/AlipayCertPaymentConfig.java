package org.cqq.openlibrary.payment.alipay.config;

import com.alipay.v3.util.model.AlipayConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 支付宝证书支付配置
 *
 * @author Qingquan
 */
@Data
public class AlipayCertPaymentConfig {
    
    // ================================ 商户 ================================
    
    /**
     * 商户应用 id
     */
    @Value("${payment.alipay.merchant.appid}")
    private String merchantAppId;
    
    /**
     * 商户私钥 (非路径)
     */
    @Value("${payment.alipay.merchant.app-private-key}")
    private String merchantAppPrivateKey;
    
    /**
     * 商户公钥证书路径
     */
    @Value("${payment.alipay.merchant.app-cert-path}")
    private String merchantAppCertPath;
    
    // ================================ 支付宝 ================================
    
    /**
     * 支付宝网关地址
     */
    @Value("${payment.alipay.server-url}")
    private String alipayServerUrl;
    
    /**
     * 支付宝公钥证书路径
     */
    @Value("${payment.alipay.public-cert-path}")
    private String alipayPublicCertPath;
    
    /**
     * 支付宝根证书路径
     */
    @Value("${payment.alipay.root-cert-path}")
    private String alipayRootCertPath;
    
    /**
     * 创建自动更新平台证书的配置类
     */
    public AlipayConfig createCertPaymentConfig() {
        AlipayConfig config = new AlipayConfig();
        config.setAppId(merchantAppId);
        config.setPrivateKey(merchantAppPrivateKey);
        config.setAppCertPath(merchantAppCertPath);
        config.setAlipayPublicCertPath(alipayPublicCertPath);
        config.setRootCertPath(alipayRootCertPath);
        return config;
    }
}