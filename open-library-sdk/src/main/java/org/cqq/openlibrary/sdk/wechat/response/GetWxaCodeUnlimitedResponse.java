package org.cqq.openlibrary.sdk.wechat.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;
import org.cqq.openlibrary.sdk.wechat.response.base.WechatResponse;

/**
 * 获取不限制的小程序码 API 请求
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetWxaCodeUnlimitedResponse extends WechatResponse<GetWxaCodeUnlimitedResponse> {
    
    private byte[] buffer;
    
    @Override
    public CheckedFunction<ResponseBody, GetWxaCodeUnlimitedResponse, ?> parseResponseMapper() {
        return responseBody -> {
            GetWxaCodeUnlimitedResponse response = new GetWxaCodeUnlimitedResponse();
            response.setBuffer(responseBody.bytes());
            return response;
        };
    }
}