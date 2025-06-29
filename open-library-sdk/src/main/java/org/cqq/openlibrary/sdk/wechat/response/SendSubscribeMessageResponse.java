package org.cqq.openlibrary.sdk.wechat.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;
import org.cqq.openlibrary.sdk.wechat.response.base.WechatResponse;
import org.cqq.openlibrary.common.util.JSONUtils;

/**
 * 发送订阅消息 API 响应
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SendSubscribeMessageResponse extends WechatResponse<SendSubscribeMessageResponse> {
    
    @Override
    public CheckedFunction<ResponseBody, SendSubscribeMessageResponse, ?> parseResponseMapper() {
        return responseBody -> JSONUtils.parse(responseBody.string(), SendSubscribeMessageResponse.class);
    }
}
