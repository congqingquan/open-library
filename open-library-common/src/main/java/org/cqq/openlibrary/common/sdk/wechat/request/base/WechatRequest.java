package org.cqq.openlibrary.common.sdk.wechat.request.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.cqq.openlibrary.common.sdk.wechat.WechatApiEnum;
import org.cqq.openlibrary.common.sdk.wechat.response.base.WechatResponse;

import java.util.function.Supplier;

/**
 * 微信请求
 *
 * @author Qingquan
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class WechatRequest<Response extends WechatResponse<Response>> {
    
    @JsonIgnore
    private String appid;
    
    @JsonIgnore
    private String appSecret;
    
    @JsonIgnore
    private String accessToken;
    
    @JsonIgnore
    private boolean checkResponse = true;
    
    public abstract WechatApiEnum apiEnum();
    
    public abstract Supplier<Response> responseSupplier();
}
