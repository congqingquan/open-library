package org.cqq.openlibrary.notification.exception;

import org.cqq.openlibrary.common.exception.ExceptionROption;
import org.cqq.openlibrary.common.exception.biz.BusinessException;

/**
 * Ali cloud SMS exception
 *
 * @author Qingquan
 */
public class AliCloudSMSException extends BusinessException {
    
    public AliCloudSMSException() {
    }
    
    public AliCloudSMSException(String message) {
        super(message);
    }
    
    public AliCloudSMSException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AliCloudSMSException(Throwable cause) {
        super(cause);
    }
    
    public AliCloudSMSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public ExceptionROption getBizExceptionROption() {
        return ExceptionROption.BIZ_ALI_CLOUD_EXCEPTION;
    }
}