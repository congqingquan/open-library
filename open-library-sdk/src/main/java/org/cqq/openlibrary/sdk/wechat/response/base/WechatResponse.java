package org.cqq.openlibrary.sdk.wechat.response.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;

/**
 * 微信 API 响应
 *
 * @author Qingquan
 */
@Data
public abstract class WechatResponse<Response extends WechatResponse<Response>> {
    
    @JsonProperty("errcode")
    private int errCode;
    
    @JsonProperty("errmsg")
    private String errMsg;
    
    public abstract CheckedFunction<ResponseBody, Response, ?> parseResponseMapper();
}
