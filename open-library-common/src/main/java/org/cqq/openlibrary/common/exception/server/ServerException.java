package org.cqq.openlibrary.common.exception.server;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * Server exception
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerException extends RuntimeException {
    
    public ServerException() {
    }
    
    public ServerException(String message) {
        super(message);
    }
    
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServerException(Throwable cause) {
        super(cause);
    }
    
    public ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getServerExceptionROption() {
        return ExceptionROption.SERVER_EXCEPTION;
    }
}