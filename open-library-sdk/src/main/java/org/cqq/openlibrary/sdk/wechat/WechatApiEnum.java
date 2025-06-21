package org.cqq.openlibrary.sdk.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.openlibrary.common.enums.HttpMethodEnum;
import org.cqq.openlibrary.common.enums.MediaTypeEnum;

/**
 * 微信 API 枚举
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum WechatApiEnum {
    
    GET_ACCESS_TOKEN("https://api.weixin.qq.com/cgi-bin/token", HttpMethodEnum.GET, null, false),
    JS_CODE_2_SESSION("https://api.weixin.qq.com/sns/jscode2session", HttpMethodEnum.GET, null, false),
    SEND_SUBSCRIBE_MESSAGE("https://api.weixin.qq.com/cgi-bin/message/subscribe/send", HttpMethodEnum.POST, MediaTypeEnum.APPLICATION_JSON, true),
    GET_USER_PHONE_NUMBER("https://api.weixin.qq.com/wxa/business/getuserphonenumber", HttpMethodEnum.POST, MediaTypeEnum.APPLICATION_JSON, true),
    GET_WXA_CODE_UNLIMITED("https://api.weixin.qq.com/wxa/getwxacodeunlimit", HttpMethodEnum.POST, MediaTypeEnum.APPLICATION_JSON, true),
    ;
    
    private final String url;
    
    private final HttpMethodEnum method;
    
    private final MediaTypeEnum mediaType;
    
    private final Boolean needAccessToken;
}
