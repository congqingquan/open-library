package org.cqq.openlibrary.common.sdk.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cqq.openlibrary.common.sdk.wechat.WechatApiEnum;
import org.cqq.openlibrary.common.sdk.wechat.request.base.WechatGetRequest;
import org.cqq.openlibrary.common.sdk.wechat.response.JSCode2SessionResponse;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 获取访问 token API 请求
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JSCode2SessionRequest extends WechatGetRequest<JSCode2SessionResponse> {
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
    
    private String jsCode;
    
    @Override
    public WechatApiEnum apiEnum() {
        return WechatApiEnum.JS_CODE_2_SESSION;
    }
    
    @Override
    public Supplier<JSCode2SessionResponse> responseSupplier() {
        return JSCode2SessionResponse::new;
    }
    
    @Override
    public Map<String, String> buildUrlParamsMap() {
        Map<String, String> buildUrlParamsMap = super.buildBasebuildUrlParamsMap(this);
        buildUrlParamsMap.put("js_code", this.jsCode);
        buildUrlParamsMap.put("grant_type", "authorization_code");
        return buildUrlParamsMap;
    }
}
