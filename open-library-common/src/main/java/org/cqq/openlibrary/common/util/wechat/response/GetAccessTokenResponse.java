package org.cqq.openlibrary.common.util.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取服务端访问 token API 响应
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetAccessTokenResponse extends WechatResponse {
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("expires_in")
    private Long expiresIn;
}
