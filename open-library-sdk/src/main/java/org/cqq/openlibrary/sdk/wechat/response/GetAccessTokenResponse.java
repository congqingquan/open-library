package org.cqq.openlibrary.sdk.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.sdk.wechat.response.base.WechatResponse;

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
        return responseBody -> JSONUtils.parse(responseBody.string(), GetAccessTokenResponse.class);
    }
}
