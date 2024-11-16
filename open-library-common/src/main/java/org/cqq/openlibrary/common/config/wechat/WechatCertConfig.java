package org.cqq.openlibrary.common.config.wechat;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 微信证书配置
 *
 * @author Qingquan
 */
@Data
public class WechatCertConfig {
    
    // ================================ 商户 ================================
    
    /**
     * 商户号
     */
    @Value("${wechat.merchant.id:none}")
    private String merchantId;
    
    /**
     * 商户应用 id
     */
    @Value("${wechat.merchant.appid:none}")
    private String merchantAppid;
    
    /**
     * 商户应用密钥
     */
    @Value("${wechat.merchant.app-secret:none}")
    private String merchantAppSecret;
    
    /**
     * 商户 API 证书序列号
     */
    @Value("${wechat.merchant.api-cert-serial-number:none}")
    private String merchantApiCertSerialNumber;
    
    /**
     * 商户私钥文件路径
     */
    @Value("${wechat.merchant.private-key-path:none}")
    private String merchantPrivateKeyPath;
    
    /**
     * 商户 APIv3 密钥
     */
    @Value("${wechat.merchant.api-v3-key:none}")
    private String merchantApiV3Key;
}