package org.cqq.openlibrary.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cqq.openlibrary.common.annotation.Auth;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * 鉴权拦截器
 *
 * @author Qingquan
 */
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        
        Auth annotation = method.getAnnotation(Auth.class);
        if (annotation != null && !annotation.authenticate()) {
            return true;
        }
        checkAuthenticated(request, response, handler);
        
        if (annotation != null && !annotation.authorize()) {
            return true;
        }
        checkAuthorized(request, response, handler);
        
        return true;
    }
    
    /**
     * 检查登录
     */
    public void checkAuthenticated(HttpServletRequest request, HttpServletResponse response, Object handler) {
    }
    
    /**
     * 检查权限
     */
    public void checkAuthorized(HttpServletRequest request, HttpServletResponse response, Object handler) {
    }
}
