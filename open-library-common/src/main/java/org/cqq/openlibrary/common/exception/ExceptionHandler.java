package org.cqq.openlibrary.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.domain.R;
import org.cqq.openlibrary.common.exception.biz.BusinessException;
import org.cqq.openlibrary.common.exception.client.ClientException;
import org.cqq.openlibrary.common.exception.client.UnauthenticatedException;
import org.cqq.openlibrary.common.exception.client.UnauthorizedException;
import org.cqq.openlibrary.common.exception.server.ServerException;
import org.cqq.openlibrary.common.interfaces.ROption;
import org.cqq.openlibrary.common.util.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Exception handler
 *
 * @author Qingquan
 */
@Slf4j
public class ExceptionHandler {
    
    
    // =============================================== Common ===============================================
    
    private String getBaseMessage(HttpServletRequest request, ROption option) {
        return String.format("URI [%s], Response code [%s], Message [%s]",
                request.getRequestURI(),
                option.getCode(),
                option.getMessage()
        );
    }
    
    public R<?> commonHandle(ExceptionROption exceptionROption, HttpServletRequest request, Throwable throwable) {
        log.error(getBaseMessage(request, exceptionROption), throwable);
        return R.response(exceptionROption.getCode(), null, Optional.ofNullable(throwable.getMessage()).orElse(exceptionROption.getMessage()));
    }
    
    // =============================================== 1.x Client ===============================================
    
    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> handleUnauthenticatedException(HttpServletRequest request, UnauthenticatedException exception) {
        return commonHandle(ExceptionROption.CLIENT_UNAUTHENTICATED, request, exception);
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<?> handleUnauthorizedException(HttpServletRequest request, UnauthorizedException exception) {
        return commonHandle(ExceptionROption.CLIENT_UNAUTHORIZED, request, exception);
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> handleClientException(HttpServletRequest request, ClientException exception) {
        return commonHandle(exception.getClientExceptionROption(), request, exception);
    }
    
    // =============================================== 2.x Server ===============================================
    
    // Jackson mapping exception (Execute before spring validator)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {HttpMessageNotReadableException.class,})
    @ResponseStatus(HttpStatus.OK)
    public R<?> handleJsonMappingException(HttpServletRequest request, HttpMessageNotReadableException exception) {
        ExceptionROption exceptionROption = ExceptionROption.SERVER_VALIDATED_PARAM_EXCEPTION;
        String message = exceptionROption.getMessage();
        log.error(getBaseMessage(request, exceptionROption), exception);
        
        Throwable cause = exception.getCause();
        if (cause == null || !JsonMappingException.class.isAssignableFrom(cause.getClass())) {
            return R.response(exceptionROption.getCode(), null, message);
        }
        
        JsonMappingException jsonMappingException = (JsonMappingException) cause;
        List<JsonMappingException.Reference> paths = jsonMappingException.getPath();
        List<String> invalidParams = new ArrayList<>(paths.size());
        for (JsonMappingException.Reference path : paths) {
            ReflectionUtils.doWithFields(
                    path.getFrom().getClass(),
                    f -> {
                        Schema schema = f.getAnnotation(Schema.class);
                        invalidParams.add(schema != null ? String.format("%s(%s)", schema.description(), f.getName()) : f.getName());
                    },
                    f -> f.getName().equals(path.getFieldName())
            );
        }
        message = "参数不合规: " + String.join(", ", invalidParams);
        return R.response(exceptionROption.getCode(), null, message);
    }
    
    // Spring validator exception
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class, ConstraintDeclarationException.class
    })
    @ResponseStatus(HttpStatus.OK)
    public R<?> handleValidatorException(HttpServletRequest request, Throwable exception) {
        
        ExceptionROption exceptionROption = ExceptionROption.SERVER_VALIDATED_PARAM_EXCEPTION;
        String message = exceptionROption.getMessage();
        log.error(getBaseMessage(request, exceptionROption), exception);
        
        // ConstraintViolationException: ValidationException 的子类异常
        if (exception instanceof ConstraintViolationException constraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
            message = CollectionUtils.getLast(constraintViolations).map(ConstraintViolation::getMessage).orElse(null);
        }
        // ConstraintDeclarationException: ValidationException 的子类异常
        else if (exception instanceof ConstraintDeclarationException constraintDeclarationException) {
            message = constraintDeclarationException.getMessage();
        }
        // MethodArgumentNotValidException: BindException 的子类，当接口的请求类型为 application/json 时产生的参数校验异常
        // BindException: 顶级的参数校验异常
        else if (exception instanceof BindException bindException) {
            message = CollectionUtils.getLast(bindException.getBindingResult().getFieldErrors()).map(FieldError::getDefaultMessage).orElse(null);
        }
        
        return R.response(exceptionROption.getCode(), null, message);
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> handleBusinessException(HttpServletRequest request, ServerException exception) {
        return commonHandle(exception.getServerExceptionROption(), request, exception);
    }
    
    // =============================================== 3.x Business ===============================================
    
    @org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> handleBusinessException(HttpServletRequest request, BusinessException exception) {
        return commonHandle(exception.getBizExceptionROption(), request, exception);
    }
    
    // =============================================== 兜底处理 ===============================================
    
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> handleException(HttpServletRequest request, Exception exception) {
        return commonHandle(ExceptionROption.SERVER_EXCEPTION, request, exception);
    }
    
}