package org.cqq.openlibrary.common.sdk.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * 微信 API 枚举
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum WechatApiEnum {
    
    GET_ACCESS_TOKEN("https://api.weixin.qq.com/cgi-bin/token", HttpMethod.GET, null, false),
    JS_CODE_2_SESSION("https://api.weixin.qq.com/sns/jscode2session", HttpMethod.GET, null, false),
    SEND_SUBSCRIBE_MESSAGE("https://api.weixin.qq.com/cgi-bin/message/subscribe/send", HttpMethod.POST, MediaType.APPLICATION_JSON_VALUE, true),
    GET_USER_PHONE_NUMBER("https://api.weixin.qq.com/wxa/business/getuserphonenumber", HttpMethod.POST, MediaType.APPLICATION_JSON_VALUE, true),
    GET_WXA_CODE_UNLIMITED("https://api.weixin.qq.com/wxa/getwxacodeunlimit", HttpMethod.POST, MediaType.APPLICATION_JSON_VALUE, true),
    ;
    
    private final String url;
    
    private final HttpMethod method;
    
    private final String mediaType;
    
    private final Boolean needAccessToken;
}
