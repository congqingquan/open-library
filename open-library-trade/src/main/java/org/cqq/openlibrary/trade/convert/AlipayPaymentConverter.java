package org.cqq.openlibrary.trade.convert;

import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.Configuration;
import com.alipay.v3.util.model.AlipayConfig;
import org.cqq.openlibrary.trade.config.alipay.AlipayPaymentConfig;
import org.cqq.openlibrary.trade.exception.AlipayException;

/**
 * Alipay payment converter
 *
 * @author Qingquan
 */
public interface AlipayPaymentConverter {
  
    static AlipayConfig toAlipayConfig(AlipayPaymentConfig certConfig) {
        AlipayConfig config = new AlipayConfig();
        config.setAppId(certConfig.getMerchantAppId());
        config.setPrivateKey(certConfig.getMerchantAppPrivateKey());
        config.setAppCertPath(certConfig.getMerchantAppCertPath());
        config.setAlipayPublicCertPath(certConfig.getPublicCertPath());
        config.setRootCertPath(certConfig.getRootCertPath());
        return config;
    }
    
    static ApiClient toApiClient(AlipayPaymentConfig certConfig) {
        AlipayConfig config = toAlipayConfig(certConfig);
        String alipayServerUrl = certConfig.getServerUrl();
        String merchantAppId = certConfig.getMerchantAppId();
        
        ApiClient apiClient = Configuration.getDefaultApiClient();
        try {
            apiClient.setBasePath(alipayServerUrl);
            apiClient.setAlipayConfig(config);
        } catch (ApiException exception) {
            throw new AlipayException(String.format("Building merchant(appid > [%s]) ApiClient failed", merchantAppId), exception);
        }
        return apiClient;
    }
}
