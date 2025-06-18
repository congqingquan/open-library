package org.cqq.openlibrary.common.exception.server;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * Reflection runtime exception
 *
 * @author Qingquan
 */
public class ReflectionRuntimeException extends RuntimeException {
    
    public ReflectionRuntimeException() {
    }
    
    public ReflectionRuntimeException(String message) {
        super(message);
    }
    
    public ReflectionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ReflectionRuntimeException(Throwable cause) {
        super(cause);
    }
    
    public ReflectionRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getServerExceptionROption() {
        return ExceptionROption.SERVER_EXCEPTION;
    }
}
