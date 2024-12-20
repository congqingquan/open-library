package org.cqq.openlibrary.common.sdk.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;
import org.cqq.openlibrary.common.sdk.wechat.response.base.WechatResponse;
import org.cqq.openlibrary.common.util.JSONUtils;

/**
 * 获取服务端访问 token API 响应
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetAccessTokenResponse extends WechatResponse<GetAccessTokenResponse> {
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("expires_in")
    private Long expiresIn;
    
    @Override
    public CheckedFunction<ResponseBody, GetAccessTokenResponse, ?> parseResponseMapper() {
        return responseBody -> JSONUtils.parseObject(responseBody.string(), GetAccessTokenResponse.class);
    }
}
