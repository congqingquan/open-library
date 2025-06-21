package org.cqq.openlibrary.trade.util;

import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.api.AlipaySystemOauthApi;
import com.alipay.v3.api.AlipayUserInfoApi;
import com.alipay.v3.model.AlipaySystemOauthTokenModel;
import com.alipay.v3.model.AlipaySystemOauthTokenResponseModel;
import com.alipay.v3.model.AlipayUserInfoShareResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.trade.exception.AlipayException;

/**
 * Alipay ApiClient utils
 *
 * @author Qingquan
 */
@Slf4j
public class AlipayApiClientUtils {

    /**
     * 获取用户信息
     * <p>
     * 1. alipay.system.oauth.token(换取授权访问令牌)
     * 2. 根据获取的访问令牌，调用 alipay.user.info.share(支付宝会员授权信息查询接口) 查询用户信息
     * </p>
     */
    public static AlipayUserInfoShareResponseModel getAlipayUserInfo(ApiClient apiClient, String authCode) {
        // 1. 获取授权令牌
        AlipaySystemOauthTokenResponseModel authTokenResponse;
        try {
            authTokenResponse = new AlipaySystemOauthApi(apiClient)
                    .token(new AlipaySystemOauthTokenModel().code(authCode).grantType("authorization_code"));
        } catch (ApiException exception) {
            log.error("授权码 [{}] 换取授权令牌失败", authCode, exception);
            throw new AlipayException("获取支付宝用户信息失败");
        }

        // 2. 获取用户信息
        String accessToken = authTokenResponse.getAccessToken();
        log.info("授权令牌 [{}] 换取用户信息", accessToken);
        AlipayUserInfoShareResponseModel userInfoShareResponse;
        try {
            userInfoShareResponse = new AlipayUserInfoApi(apiClient).share(accessToken);
        } catch (ApiException exception) {
            log.error("授权令牌 [{}] 换取用户信息失败", accessToken, exception);
            throw new AlipayException("获取支付宝用户信息失败");
        }
        
        log.info("获取支付宝用户信息成功 [{}]", JSONUtils.toJSONString(userInfoShareResponse));
        return userInfoShareResponse;
    }
}
