package org.cqq.openlibrary.common.exception;

/**
 * 阿里云异常
 *
 * @author Qingquan
 */
public class AliYunException extends RuntimeException {
    
    public AliYunException() {
    }
    
    public AliYunException(String message) {
        super(message);
    }
    
    public AliYunException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AliYunException(Throwable cause) {
        super(cause);
    }
    
    public AliYunException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}