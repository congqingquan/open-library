package org.cqq.openlibrary.common.exception.server;

import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * OK http response exception
 *
 * @author Qingquan
 */
public class OkHttpResponseException extends ServerException {
    
    public OkHttpResponseException() {
    }
    
    public OkHttpResponseException(String message) {
        super(message);
    }
    
    public OkHttpResponseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OkHttpResponseException(Throwable cause) {
        super(cause);
    }
    
    public OkHttpResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getServerExceptionROption() {
        return ExceptionROption.SERVER_NETWORK_EXCEPTION;
    }
}