package org.cqq.oplibrary.web.exception;

/**
 * Created by Kim QQ.Cong on 2022/12/31 / 19:19
 *
 * @author: CongQingquan
 * @Description: 未授权异常
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException() {
    }
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UnauthorizedException(Throwable cause) {
        super(cause);
    }
    
    public UnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
