package org.cqq.openlibrary.common.exception.client;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * 未认证异常
 *
 * @author Qingquan
 */
public class UnauthenticatedException  extends ClientException {
    
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
    
    public ExceptionROption getClientExceptionROption() {
        return ExceptionROption.CLIENT_UNAUTHENTICATED;
    }
}
