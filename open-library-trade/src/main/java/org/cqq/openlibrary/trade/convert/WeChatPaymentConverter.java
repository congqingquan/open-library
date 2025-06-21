package org.cqq.openlibrary.trade.convert;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.cqq.openlibrary.trade.config.wechat.WeChatPaymentConfig;

/**
 * WeChat payment converter
 *
 * @author Qingquan
 */
public interface WeChatPaymentConverter {
    
    /**
     * 转为自动更新平台证书的配置类(不同商户全局只应初始化一次)
     */
    static RSAAutoCertificateConfig toRSAAutoCertificateConfig(WeChatPaymentConfig certConfig) {
        return new RSAAutoCertificateConfig
                .Builder()
                .merchantId(certConfig.getMerchantId())
                .merchantSerialNumber(certConfig.getMerchantApiCertSerialNumber())
                .privateKeyFromPath(certConfig.getMerchantPrivateKeyPath())
                .apiV3Key(certConfig.getMerchantApiV3Key())
                .build();
    }
}
