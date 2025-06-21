package org.cqq.openlibrary.sdk.wechat.exception;

import org.cqq.openlibrary.common.exception.ExceptionROption;
import org.cqq.openlibrary.common.exception.biz.BusinessException;

/**
 * Wechat sdk exception
 *
 * @author Qingquan
 */
public class WechatSDKException extends BusinessException {
    
    public WechatSDKException() {
    }
    
    public WechatSDKException(String message) {
        super(message);
    }
    
    public WechatSDKException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WechatSDKException(Throwable cause) {
        super(cause);
    }
    
    public WechatSDKException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getBizExceptionROption() {
        return ExceptionROption.BIZ_WECHAT_EXCEPTION;
    }
}