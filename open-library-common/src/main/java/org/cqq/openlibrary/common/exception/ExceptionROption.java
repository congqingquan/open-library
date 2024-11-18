package org.cqq.openlibrary.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.openlibrary.common.interfaces.ROption;

/**
 * Exception response option
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum ExceptionROption implements ROption {
    
    // Success
    SUCCESS(0, "成功"),
    
    // Client
    CLIENT_EXCEPTION(1000, "客户端异常"),
    CLIENT_UNAUTHENTICATED(1001, "未认证"),
    CLIENT_UNAUTHORIZED(1002, "未授权"),
    CLIENT_VALIDATED_PARAM_EXCEPTION(1003, "请求参数异常"),
    
    // Server
    SERVER_EXCEPTION(2000, "服务异常"),
    SERVER_NETWORK_EXCEPTION(2001, "服务异常"),
    
    // Business
    BIZ_EXCEPTION(3000, "业务异常"),
    BIZ_WECHAT_EXCEPTION(3001, "微信异常"),
    BIZ_ALIPAY_EXCEPTION(3002, "支付宝异常"),
    BIZ_ALI_YUN_EXCEPTION(3002, "阿里云异常")
    ;
    
    private final Integer code;
    
    private final String message;
}