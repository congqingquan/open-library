package org.cqq.openlibrary.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.domain.R;
import org.cqq.openlibrary.common.domain.ROption;
import org.cqq.openlibrary.common.domain.WebServerROption;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Common exception handler
 *
 * @author Qingquan
 */
@Slf4j
public class CommonExceptionHandler {
    
    private static final String LOG_EXCEPTION_FORMAT = "ExceptionHandler > URI [%s], Response code [%s], Message [%s]";
    
    private String getBaseMessage(HttpServletRequest request, ROption option) {
        return String.format(LOG_EXCEPTION_FORMAT,
                request.getRequestURI(),
                option.getCode(),
                option.getMessage()
        );
    }
    
    private String concatFieldError(List<FieldError> fieldErrorList) {
        StringBuilder builder = new StringBuilder();
        fieldErrorList.forEach(fieldError ->
                builder.append("[")
                        .append(fieldError.getField())
                        .append(" > ")
                        .append(fieldError.getDefaultMessage())
                        .append("]")
                        .append(", ")
        );
        return builder.substring(0, builder.length() - 2);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public R<?> handleUnauthenticatedException(HttpServletRequest request, UnauthenticatedException exception) {
        log.error(getBaseMessage(request, WebServerROption.UNAUTHENTICATED), exception);
        return new R<>(WebServerROption.UNAUTHENTICATED.getCode(), null, exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public R<?> handleUnauthorizedException(HttpServletRequest request, UnauthorizedException exception) {
        log.error(getBaseMessage(request, WebServerROption.UNAUTHORIZED), exception);
        return new R<>(WebServerROption.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R<?> handleBusinessException(HttpServletRequest request, BusinessException exception) {
        log.error(getBaseMessage(request, WebServerROption.BUSINESS_EXCEPTION), exception);
        return new R<>(WebServerROption.BUSINESS_EXCEPTION.getCode(), null, exception.getMessage());
    }
    
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class,})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R<?> handleValidatedException(HttpServletRequest request, Throwable exception) {
        log.error(getBaseMessage(request, WebServerROption.VALIDATED_EXCEPTION), exception);
        // ValidationException 的子类异常 ConstraintViolationException
        if (exception instanceof ConstraintViolationException constraintViolationException) {
            /*
             * ConstraintViolationException的e.getMessage()形如: {方法名}.{参数名}: {message} 这里只需要取后面的message即可
             */
            Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
            String message = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
            return new R<>(WebServerROption.VALIDATED_EXCEPTION.getCode(), null, message);
        }
        // MethodArgumentNotValidException: BindException 的子类，当接口的请求类型为 application/json 时产生的参数校验异常
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            return new R<>(WebServerROption.VALIDATED_EXCEPTION.getCode(), null, concatFieldError(bindingResult.getFieldErrors()));
        }
        // BindException: 顶级的参数校验异常
        else if (exception instanceof BindException bindException) {
            BindingResult bindingResult = bindException.getBindingResult();
            return new R<>(WebServerROption.VALIDATED_EXCEPTION.getCode(), null, concatFieldError(bindingResult.getFieldErrors()));
        }
        return new R<>(WebServerROption.VALIDATED_EXCEPTION);
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R<?> handleException(HttpServletRequest request, Exception exception) {
        log.error(getBaseMessage(request, WebServerROption.SERVER_INNER_EXCEPTION), exception);
        return new R<>(WebServerROption.SERVER_INNER_EXCEPTION);
    }
}