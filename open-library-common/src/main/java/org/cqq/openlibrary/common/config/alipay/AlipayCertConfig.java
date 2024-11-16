package org.cqq.openlibrary.common.config.alipay;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 支付宝证书配置
 *
 * @author Qingquan
 */
@Data
public class AlipayCertConfig {
    
    // ================================ 商户 ================================
    
    /**
     * 商户应用 id
     */
    @Value("${alipay.merchant.appid:none}")
    private String merchantAppId;
    
    /**
     * 商户私钥 (非路径)
     */
    @Value("${alipay.merchant.app-private-key:none}")
    private String merchantAppPrivateKey;
    
    /**
     * 商户公钥证书路径
     */
    @Value("${alipay.merchant.app-cert-path:none}")
    private String merchantAppCertPath;
    
    // ================================ 支付宝 ================================
    
    /**
     * 支付宝网关地址
     */
    @Value("${alipay.server-url:none}")
    private String alipayServerUrl;
    
    /**
     * 支付宝公钥证书路径
     */
    @Value("${alipay.public-cert-path:none}")
    private String alipayPublicCertPath;
    
    /**
     * 支付宝根证书路径
     */
    @Value("${alipay.root-cert-path:none}")
    private String alipayRootCertPath;
}