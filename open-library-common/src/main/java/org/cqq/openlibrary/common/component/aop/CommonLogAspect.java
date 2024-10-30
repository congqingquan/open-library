package org.cqq.openlibrary.common.component.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.cqq.openlibrary.common.jwt.JWSUserUtils;
import org.cqq.openlibrary.common.util.ArrayUtils;
import org.cqq.openlibrary.common.util.HttpContext;
import org.cqq.openlibrary.common.util.NetUtils;
import org.slf4j.MDC;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Common 日志切面
 *
 * @author Qingquan
 */
@Slf4j
public class CommonLogAspect {
    
    public Object webLogAround(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        if (!(signature instanceof MethodSignature methodSignature)) {
            return point.proceed();
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
        log.info("用户 [{}] IP [{}] 进入 [{}.{}] 方法 {} 入参:{} {}",
            userId == null ? "No user id" : userId, requestIp, className, methodName,
            getLineSeparator(2),
            requestParamMap,
            getLineSeparator(0));

        // 2. 业务方法执行
        // 1) 计时(毫秒)
        long start = System.currentTimeMillis();
        // 2) 执行
        Object result;
        try {
            result = point.proceed();
            // 3. 记录出参
            long totalTimeMillis = System.currentTimeMillis() - start;
            log.info("用户 [{}] IP [{}] 结束 [{}.{}] 方法调用 耗时 {}ms {} 出参{} {}",
                userId == null ? "No user id" : userId,
                requestIp, className, methodName, totalTimeMillis,
                getLineSeparator(1),
                result == null ? null : result.toString(),
                getLineSeparator(1));
        } finally {
            // 4. 清除traceId
            clearTrackId();
        }
        return result;
    }

    /**
     * 获取请求参数Map
     */
    private Map<String, Object> getRequestParamMap(ProceedingJoinPoint point, MethodSignature methodSignature) {
        final Map<String, Object> requestParams = new HashMap<>(16);
        Object[] args = point.getArgs();
        Method method = methodSignature.getMethod();
        StandardReflectionParameterNameDiscoverer parameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
        if (ArrayUtils.isNotEmpty(args) && ArrayUtils.isNotEmpty(paramNames)) {
            for (int i = 0; i < args.length; i++) {
                requestParams.put(paramNames[i], args[i]);
            }
        }
        return requestParams;
    }

    /**
     * 获取换行符
     */
    private String getLineSeparator(Integer count) {
        return String.valueOf(System.lineSeparator()).repeat(Math.max(0, count));
    }

    /**
     * 设置TraceId
     */
    private void setTrackId() {
        MDC.put("TraceId", UUID.randomUUID().toString().replace("-", ""));
    }

    /**
     * 清除TraceId
     */
    private void clearTrackId() {
        MDC.clear();
    }
}