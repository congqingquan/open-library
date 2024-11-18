package org.cqq.openlibrary.common.exception.client;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * 未授权异常
 *
 * @author Qingquan
 */
public class UnauthorizedException extends ClientException {
    
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
    
    public ExceptionROption getClientExceptionROption() {
        return ExceptionROption.CLIENT_UNAUTHORIZED;
    }
}
