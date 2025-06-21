package org.cqq.openlibrary.trade.exception;

import org.cqq.openlibrary.common.exception.ExceptionROption;
import org.cqq.openlibrary.common.exception.biz.BusinessException;

/**
 * Wechat payment exception
 *
 * @author Qingquan
 */
public class WechatPaymentException extends BusinessException {
    
    public WechatPaymentException() {
    }
    
    public WechatPaymentException(String message) {
        super(message);
    }
    
    public WechatPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WechatPaymentException(Throwable cause) {
        super(cause);
    }
    
    public WechatPaymentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getBizExceptionROption() {
        return ExceptionROption.BIZ_WECHAT_EXCEPTION;
    }
}