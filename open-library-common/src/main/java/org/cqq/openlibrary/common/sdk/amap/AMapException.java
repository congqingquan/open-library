package org.cqq.openlibrary.common.sdk.amap;

/**
 * AMapException
 *
 * @author Qingquan
 */
public class AMapException extends RuntimeException {
    
    public AMapException() {
    }
    
    public AMapException(String message) {
        super(message);
    }
    
    public AMapException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AMapException(Throwable cause) {
        super(cause);
    }
    
    public AMapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
