package org.cqq.openlibrary.storage.handler.replaceurl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.cqq.openlibrary.storage.annotation.ReplaceURL;
import org.cqq.openlibrary.storage.handler.replaceurl.request.RequestReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.response.ResponseReplaceURLHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;

/**
 * Request replace url handler
 *
 * @author Qingquan
 */
@Slf4j
@AllArgsConstructor
public class ReplaceURLHandler implements MethodInterceptor {
    
    private final Collection<RequestReplaceURLHandler> requestReplaceURLHandlers;
    
    private final Collection<ResponseReplaceURLHandler> responseReplaceURLHandlers;
    
    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] arguments = invocation.getArguments();
        for (RequestReplaceURLHandler requestReplaceURLHandler : requestReplaceURLHandlers) {
            requestReplaceURLHandler.replaceURL(method, parameters, arguments);
        }
        // return directly if there are no ReplaceURL annotation on Method
        Object returnValue = invocation.proceed();
        if (!method.isAnnotationPresent(ReplaceURL.class)) {
            return returnValue;
        }
        // Handle returnValue
        for (ResponseReplaceURLHandler responseReplaceURLHandler : responseReplaceURLHandlers) {
            returnValue = responseReplaceURLHandler.replaceURL(method, returnValue);
        }
        return returnValue;
    }
}
