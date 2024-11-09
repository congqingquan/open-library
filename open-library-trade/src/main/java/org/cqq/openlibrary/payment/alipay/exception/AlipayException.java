package org.cqq.openlibrary.payment.alipay.exception;

/**
 * 支付宝异常
 *
 * @author Qingquan
 */
public class AlipayException extends RuntimeException {
    
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
}