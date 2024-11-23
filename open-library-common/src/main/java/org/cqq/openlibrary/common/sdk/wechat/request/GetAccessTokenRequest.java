package org.cqq.openlibrary.common.sdk.wechat.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cqq.openlibrary.common.sdk.wechat.WechatApiEnum;
import org.cqq.openlibrary.common.sdk.wechat.request.base.WechatGetRequest;
import org.cqq.openlibrary.common.sdk.wechat.response.GetAccessTokenResponse;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 获取访问 token API 请求
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetAccessTokenRequest extends WechatGetRequest<GetAccessTokenResponse> {
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html
    
    @Override
    public WechatApiEnum apiEnum() {
        return WechatApiEnum.GET_ACCESS_TOKEN;
    }
    
    @Override
    public Supplier<GetAccessTokenResponse> responseSupplier() {
        return GetAccessTokenResponse::new;
    }
    
    @Override
    public Map<String, String> buildUrlParamsMap() {
        Map<String, String> buildUrlParamsMap = super.buildBasebuildUrlParamsMap(this);
        buildUrlParamsMap.put("grant_type", "client_credential");
        return buildUrlParamsMap;
    }
    
    public static GetAccessTokenRequest create(String appid, String appSecret) {
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest();
        getAccessTokenRequest.setAppid(appid);
        getAccessTokenRequest.setAppSecret(appSecret);
        return getAccessTokenRequest;
    }
}
