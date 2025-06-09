package org.cqq.openlibrary.common.component.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.cqq.openlibrary.common.annotation.Ignore;
import org.cqq.openlibrary.common.jwt.JWSUserUtils;
import org.cqq.openlibrary.common.util.ArrayUtils;
import org.cqq.openlibrary.common.util.HttpContext;
import org.cqq.openlibrary.common.util.MapUtils;
import org.cqq.openlibrary.common.util.NetUtils;
import org.slf4j.MDC;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Log aspect
 *
 * @author Qingquan
 */
@Slf4j
public class LogAspect {
    
    public static final String ADDITIONAL_IGNORE_REQUEST_PARAM = "ADDITIONAL_IGNORE_REQUEST_PARAM";
    
    public static final String ADDITIONAL_IGNORE_RESPONSE_PARAM = "ADDITIONAL_IGNORE_RESPONSE_PARAM";
    
    public Object webLogAround(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        if (!(signature instanceof MethodSignature methodSignature)) {
            return point.proceed();
        }
        // 处理忽略逻辑
        Ignore ignore = methodSignature.getMethod().getAnnotation(Ignore.class);
        boolean ignoreRequestParam = false;
        boolean ignoreResponseParam = false;
        if (ignore != null) {
            for (String additional : ignore.additional()) {
                if (ADDITIONAL_IGNORE_REQUEST_PARAM.equals(additional)) {
                    ignoreRequestParam = true;
                }
                else if (ADDITIONAL_IGNORE_RESPONSE_PARAM.equals(additional)) {
                    ignoreResponseParam = true;
                }
            }
            // 忽略:
            // 1. 添加了 ignore 注解但没有指定忽略策略(默认为忽略所有)
            // 2. 添加了 ignore 注解但指定了全部的忽略策略(手动设置为忽略所有)
            if (ArrayUtils.isEmpty(ignore.additional()) || (ignoreRequestParam && ignoreResponseParam)) {
                return point.proceed();
            }
        }
        // 1. 记录入参
        // 1) 设置traceId
        setTrackId();
        // 2) 登录用户
        Long userId = JWSUserUtils.getNullableUserIdLong();
        // 3) 方法形参
        Map<String, Object> requestParamMap = getRequestParamMap(point, methodSignature);
        // 4) 其他参数
        String requestIp = NetUtils.getIpAddress(HttpContext.getRequest());
        String className = point.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        if (!ignoreRequestParam) {
            log.info("用户 [{}] IP [{}] 进入 [{}.{}] 方法 {} 入参 [{}] {}",
                    userId == null ? "No user id" : userId, requestIp, className, methodName,
                    getLineSeparator(2),
                    MapUtils.isEmpty(requestParamMap) ? "" : requestParamMap,
                    getLineSeparator(0));
        }
        
        // 2. 业务方法执行
        // 1) 计时(毫秒)
        long start = System.currentTimeMillis();
        // 2) 执行
        Object result;
        try {
            result = point.proceed();
            // 3. 记录出参
            long totalTimeMillis = System.currentTimeMillis() - start;
            if (!ignoreResponseParam) {
                log.info("用户 [{}] IP [{}] 结束 [{}.{}] 方法调用 耗时 {}ms {} 出参 [{}] {}",
                        userId == null ? "No user id" : userId,
                        requestIp, className, methodName, totalTimeMillis,
                        getLineSeparator(1),
                        Optional.ofNullable(result).map(Object::toString).orElse(""),
                        getLineSeparator(1));
            }
        } finally {
            // 4. 清除traceId
            clearTrackId();
        }
        return result;
    }
    
    private Map<String, Object> getRequestParamMap(ProceedingJoinPoint point, MethodSignature methodSignature) {
        final Map<String, Object> requestParams = new HashMap<>(16);
        Object[] args = point.getArgs();
        if (ArrayUtils.isEmpty(args)) {
            return requestParams;
        }
        StandardReflectionParameterNameDiscoverer parameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames == null) {
            return requestParams;
        }
        for (int i = 0; i < args.length; i++) {
            requestParams.put(paramNames[i], args[i]);
        }
        return requestParams;
    }
    
    private String getLineSeparator(Integer count) {
        return String.valueOf(System.lineSeparator()).repeat(Math.max(0, count));
    }
    
    private void setTrackId() {
        MDC.put("TraceId", UUID.randomUUID().toString().replace("-", ""));
    }
    
    private void clearTrackId() {
        MDC.clear();
    }
}