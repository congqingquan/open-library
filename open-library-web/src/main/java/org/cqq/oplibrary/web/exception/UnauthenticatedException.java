package org.cqq.oplibrary.web.exception;

/**
 * Created by Kim QQ.Cong on 2022/12/31 / 19:19
 *
 * @author: CongQingquan
 * @Description: 未认证异常
 */
public class UnauthenticatedException  extends RuntimeException {
    
    public UnauthenticatedException() {
    }
    
    public UnauthenticatedException(String message) {
        super(message);
    }
    
    public UnauthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UnauthenticatedException(Throwable cause) {
        super(cause);
    }
    
    public UnauthenticatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
