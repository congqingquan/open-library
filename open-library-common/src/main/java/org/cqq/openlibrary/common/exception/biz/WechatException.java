package org.cqq.openlibrary.common.exception.biz;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * 微信异常
 *
 * @author Qingquan
 */
public class WechatException extends BusinessException {
    
    public WechatException() {
    }
    
    public WechatException(String message) {
        super(message);
    }
    
    public WechatException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WechatException(Throwable cause) {
        super(cause);
    }
    
    public WechatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getBizExceptionROption() {
        return ExceptionROption.BIZ_WECHAT_EXCEPTION;
    }
}