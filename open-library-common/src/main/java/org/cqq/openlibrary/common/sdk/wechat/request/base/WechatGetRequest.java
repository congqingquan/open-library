package org.cqq.openlibrary.common.sdk.wechat.request.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cqq.openlibrary.common.sdk.wechat.response.base.WechatResponse;
import org.cqq.openlibrary.common.util.MapUtils;

import java.util.Map;

/**
 * 微信 GET 请求
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class WechatGetRequest<Response extends WechatResponse<Response>> extends WechatRequest<Response> {
    
    public abstract Map<String, String> buildUrlParamsMap();
    
    protected Map<String, String> buildBasebuildUrlParamsMap(WechatGetRequest<?> request) {
        return MapUtils.Builder.<String, String>builder()
                .put("appid", request.getAppid())
                .put("secret", request.getAppSecret())
                .build();
    }
}