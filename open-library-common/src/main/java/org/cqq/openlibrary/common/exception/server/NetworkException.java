package org.cqq.openlibrary.common.exception.server;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * Network exception
 *
 * @author Qingquan
 */
public class NetworkException extends ServerException {
    
    public NetworkException() {
    }
    
    public NetworkException(String message) {
        super(message);
    }
    
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NetworkException(Throwable cause) {
        super(cause);
    }
    
    public NetworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getServerExceptionROption() {
        return ExceptionROption.SERVER_NETWORK_EXCEPTION;
    }
}