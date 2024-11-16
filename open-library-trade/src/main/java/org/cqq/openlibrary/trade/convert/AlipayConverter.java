package org.cqq.openlibrary.trade.convert;

import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.Configuration;
import com.alipay.v3.util.model.AlipayConfig;
import org.cqq.openlibrary.common.config.alipay.AlipayCertConfig;
import org.cqq.openlibrary.common.exception.AlipayException;

/**
 * 支付宝转换器
 *
 * @author Qingquan
 */
public interface AlipayConverter {
  
    static AlipayConfig certConfig2AlipayConfig(AlipayCertConfig certConfig) {
        AlipayConfig config = new AlipayConfig();
        config.setAppId(certConfig.getMerchantAppId());
        config.setPrivateKey(certConfig.getMerchantAppPrivateKey());
        config.setAppCertPath(certConfig.getMerchantAppCertPath());
        config.setAlipayPublicCertPath(certConfig.getAlipayPublicCertPath());
        config.setRootCertPath(certConfig.getAlipayRootCertPath());
        return config;
    }
    
    static ApiClient certConfig2ApiClient(AlipayCertConfig certConfig) {
        AlipayConfig config = certConfig2AlipayConfig(certConfig);
        String alipayServerUrl = certConfig.getAlipayServerUrl();
        String merchantAppId = certConfig.getMerchantAppId();
        
        ApiClient apiClient = Configuration.getDefaultApiClient();
        try {
            apiClient.setBasePath(alipayServerUrl);
            apiClient.setAlipayConfig(config);
        } catch (ApiException exception) {
            throw new AlipayException(String.format("构建 merchant appid [%s] 的 ApiClient 失败", merchantAppId), exception);
        }
        return apiClient;
    }
}
