package org.cqq.openlibrary.payment.wechat.exception;

/**
 * 微信异常
 *
 * @author Qingquan
 */
public class WechatException extends RuntimeException {
    
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
}