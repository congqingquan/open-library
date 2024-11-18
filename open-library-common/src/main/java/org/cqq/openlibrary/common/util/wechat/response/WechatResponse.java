package org.cqq.openlibrary.common.util.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信 API 响应
 *
 * @author Qingquan
 */
@Data
public class WechatResponse {
    
    @JsonProperty("errcode")
    private int errCode;
    
    @JsonProperty("errmsg")
    private String errMsg;
}
