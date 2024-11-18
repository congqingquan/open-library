package org.cqq.openlibrary.common.exception.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * Client exception
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientException extends RuntimeException {
    
    public ClientException() {
    }
    
    public ClientException(String message) {
        super(message);
    }
    
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ClientException(Throwable cause) {
        super(cause);
    }
    
    public ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getClientExceptionROption() {
        return ExceptionROption.CLIENT_EXCEPTION;
    }
}