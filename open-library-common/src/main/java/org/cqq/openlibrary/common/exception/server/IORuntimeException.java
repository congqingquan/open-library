package org.cqq.openlibrary.common.exception.server;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * IO runtime exception
 *
 * @author Qingquan
 */
public class IORuntimeException extends RuntimeException {
    
    public IORuntimeException() {
    }
    
    public IORuntimeException(String message) {
        super(message);
    }
    
    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public IORuntimeException(Throwable cause) {
        super(cause);
    }
    
    public IORuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getServerExceptionROption() {
        return ExceptionROption.SERVER_NETWORK_EXCEPTION;
    }
}