package org.cqq.openlibrary.payment.wechat.cache;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.payment.wechat.config.WechatCertPaymentConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信证书支付配置缓存
 *
 * @author Qingquan
 */
@Slf4j
public class WechatCertPaymentConfigCache {
    
    private static final Map<String, RSAAutoCertificateConfig> rsaAutoCertConfigMap = new ConcurrentHashMap<>();
    
    // Note 从 v0.2.10 开始，我们不再限制每个商户号只能创建一个 RSAAutoCertificateConfig / https://github.com/wechatpay-apiv3/wechatpay-java
    public static RSAAutoCertificateConfig put(WechatCertPaymentConfig config) {
        return rsaAutoCertConfigMap.put(config.getMerchantId(), config.createRsaAutoCertConfig());
    }
    
    public static RSAAutoCertificateConfig get(String merchantId) {
        return rsaAutoCertConfigMap.get(merchantId);
    }
}
