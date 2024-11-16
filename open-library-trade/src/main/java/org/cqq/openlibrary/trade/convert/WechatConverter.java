package org.cqq.openlibrary.trade.convert;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.cqq.openlibrary.common.config.wechat.WechatCertConfig;

/**
 * 微信转换器
 *
 * @author Qingquan
 */
public interface WechatConverter {
    
    /**
     * 转为自动更新平台证书的配置类(不同商户全局只应初始化一次)
     */
    static RSAAutoCertificateConfig certConfig2RSAAutoCertificateConfig(WechatCertConfig certConfig) {
        return new RSAAutoCertificateConfig
                .Builder()
                .merchantId(certConfig.getMerchantId())
                .merchantSerialNumber(certConfig.getMerchantApiCertSerialNumber())
                .privateKeyFromPath(certConfig.getMerchantPrivateKeyPath())
                .apiV3Key(certConfig.getMerchantApiV3Key())
                .build();
    }
}
