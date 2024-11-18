package org.cqq.openlibrary.common.exception.biz;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cqq.openlibrary.common.exception.ExceptionROption;

/**
 * Business exception
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    
    public BusinessException() {
    }
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BusinessException(Throwable cause) {
        super(cause);
    }
    
    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getBizExceptionROption() {
        return ExceptionROption.BIZ_EXCEPTION;
    }
}