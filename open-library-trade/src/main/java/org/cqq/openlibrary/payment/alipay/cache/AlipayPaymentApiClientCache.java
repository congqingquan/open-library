package org.cqq.openlibrary.payment.alipay.cache;

import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.Configuration;
import com.alipay.v3.util.model.AlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.payment.alipay.config.AlipayCertPaymentConfig;
import org.cqq.openlibrary.payment.alipay.exception.AlipayException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付宝支付 ApiClient 缓存
 *
 * @author Qingquan
 */
@Slf4j
public class AlipayPaymentApiClientCache {
    
    private static final Map<String, ApiClient> apiClientMap = new ConcurrentHashMap<>();
    
    public static ApiClient put(AlipayCertPaymentConfig config) {
        AlipayConfig certPaymentConfig = config.createCertPaymentConfig();
        String alipayServerUrl = config.getAlipayServerUrl();
        String merchantAppId = config.getMerchantAppId();
        
        ApiClient apiClient = Configuration.getDefaultApiClient();
        try {
            apiClient.setBasePath(alipayServerUrl);
            apiClient.setAlipayConfig(certPaymentConfig);
        } catch (ApiException exception) {
            throw new AlipayException(String.format("构建 merchant appid [%s] 的 ApiClient 失败", merchantAppId), exception);
        }
        return apiClientMap.put(merchantAppId, apiClient);
    }
    
    public static ApiClient get(String appid) {
        return apiClientMap.get(appid);
    }
}
