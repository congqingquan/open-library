package org.cqq.openlibrary.common.sdk.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cqq.openlibrary.common.sdk.wechat.WechatApiEnum;
import org.cqq.openlibrary.common.sdk.wechat.request.base.WechatPostRequest;
import org.cqq.openlibrary.common.sdk.wechat.response.SendSubscribeMessageResponse;

import java.util.function.Supplier;

/**
 * 发送订阅消息 API 请求
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SendSubscribeMessageRequest extends WechatPostRequest<SendSubscribeMessageResponse> {
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/sendMessage.html
    
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
    
    @Override
    public WechatApiEnum apiEnum() {
        return WechatApiEnum.SEND_SUBSCRIBE_MESSAGE;
    }
    
    @Override
    public Supplier<SendSubscribeMessageResponse> responseSupplier() {
        return SendSubscribeMessageResponse::new;
    }
}
