package org.cqq.openlibrary.common.exception.server;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * SQL runtime exception
 *
 * @author Qingquan
 */
public class SQLRuntimeException extends ServerException {
    
    public SQLRuntimeException() {
    }
    
    public SQLRuntimeException(String message) {
        super(message);
    }
    
    public SQLRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SQLRuntimeException(Throwable cause) {
        super(cause);
    }
    
    public SQLRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getServerExceptionROption() {
        return ExceptionROption.SERVER_NETWORK_EXCEPTION;
    }
}