package org.cqq.openlibrary.trade.exception;

import org.cqq.openlibrary.common.exception.ExceptionROption;
import org.cqq.openlibrary.common.exception.biz.BusinessException;

/**
 * Alipay exception
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