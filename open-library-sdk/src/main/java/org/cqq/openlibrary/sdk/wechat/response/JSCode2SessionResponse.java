package org.cqq.openlibrary.sdk.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;
import org.cqq.openlibrary.sdk.wechat.response.base.WechatResponse;
import org.cqq.openlibrary.common.util.JSONUtils;

/**
 * JsCode2session API 响应
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JSCode2SessionResponse extends WechatResponse<JSCode2SessionResponse> {
    
    private String openid;
    
    @JsonProperty("unionid")
    private String unionId;
    
    @JsonProperty("session_key")
    private String sessionKey;
    
    @Override
    public CheckedFunction<ResponseBody, JSCode2SessionResponse, ?> parseResponseMapper() {
        return responseBody -> JSONUtils.parse(responseBody.string(), JSCode2SessionResponse.class);
    }
}
