package org.cqq.openlibrary.common.exception.biz;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * 支付宝异常
 *
 * @author Qingquan
 */
public class AlipayException extends BusinessException {
    
    public AlipayException() {
    }
    
    public AlipayException(String message) {
        super(message);
    }
    
    public AlipayException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AlipayException(Throwable cause) {
        super(cause);
    }
    
    public AlipayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getBizExceptionROption() {
        return ExceptionROption.BIZ_ALIPAY_EXCEPTION;
    }
}