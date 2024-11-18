package org.cqq.openlibrary.common.util.wechat.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 发送订阅消息 API 请求
 *
 * @author Qingquan
 */
@Data
public class SendSubscribeMessageRequest {
    
    // url param
    @JsonIgnore
    private String accessToken;
    
    // request json body
    @JsonProperty("template_id")
    private String templateId;
    private String page;
    @JsonProperty("touser")
    private String toUser;
    // 必须为 JSON 格式的 String
    private Object data;
    @JsonProperty("miniprogram_state")
    private String miniProgramState;
    private String lang;
}
