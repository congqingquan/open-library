package org.cqq.openlibrary.common.util.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * JsCode2session API 响应
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JSCode2SessionResponse extends WechatResponse {
    
    private String openid;
    
    @JsonProperty("unionid")
    private String unionId;
    
    @JsonProperty("session_key")
    private String sessionKey;
}
