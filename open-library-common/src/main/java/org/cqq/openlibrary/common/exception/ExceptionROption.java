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
    
    // Code : Type_SubType_Event
    
    // Client
    CLIENT_EXCEPTION(ExceptionTypeEnum.CLIENT, 1_0000_0000L, "客户端异常"),
    CLIENT_UNAUTHENTICATED(ExceptionTypeEnum.CLIENT, 1_0000_0001L,  "未认证"),
    CLIENT_UNAUTHORIZED(ExceptionTypeEnum.CLIENT, 1_0000_0002L,"未授权"),
    CLIENT_VALIDATED_PARAM_EXCEPTION(ExceptionTypeEnum.CLIENT, 1_0000_0003L, "请求参数异常"),
    
    // Server
    SERVER_EXCEPTION(ExceptionTypeEnum.SERVER, 2_0000_0000L, "服务端异常"),
    SERVER_NETWORK_EXCEPTION(ExceptionTypeEnum.SERVER, 2_0000_0001L, "网络异常"),
    IO_EXCEPTION(ExceptionTypeEnum.SERVER, 2_0000_0002L, "IO异常"),
    
    // Business
    BIZ_EXCEPTION(ExceptionTypeEnum.BUSINESS, 3_0000_0000L, "业务异常"),
    BIZ_WECHAT_EXCEPTION(ExceptionTypeEnum.BUSINESS, 3_0001_0000L, "微信异常"),
    BIZ_ALIPAY_EXCEPTION(ExceptionTypeEnum.BUSINESS, 3_0002_0000L, "支付宝异常"),
    BIZ_ALI_YUN_EXCEPTION(ExceptionTypeEnum.BUSINESS, 3_0003_0000L, "阿里云异常")
    ;
    
    
    private final ExceptionTypeEnum exceptionTypeEnum;
    
    private final Long code;
    
    private final String message;
    
    @Getter
    @AllArgsConstructor
    public enum ExceptionTypeEnum {
        CLIENT,
        SERVER,
        BUSINESS
    }
}