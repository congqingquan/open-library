package org.cqq.openlibrary.storage.controlleradvice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.util.CollectionUtils;
import org.cqq.openlibrary.storage.StorageProvider;
import org.cqq.openlibrary.storage.annotation.ReplaceURL;
import org.cqq.openlibrary.storage.annotation.ReplaceURLAction;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Replace url response handler
 *
 * @author Qingquan
 */
@Slf4j
@AllArgsConstructor
public abstract class ReplaceURLResponseHandler implements ResponseBodyAdvice<Object> {
    
    // Get a storage instance dynamically
    private final StorageProvider storageProvider;
    
    @Override
    public boolean supports(@NotNull MethodParameter returnType,
                            @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        return method != null && (
                method.isAnnotationPresent(ReplaceURLAction.class) || method.getDeclaringClass().isAnnotationPresent(ReplaceURLAction.class)
        );
    }
    
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request,
                                  @NotNull ServerHttpResponse response) {
        Collection<?> objects = destructReplaceObjets(body, returnType, selectedContentType, selectedConverterType, request, response);
        if (CollectionUtils.isNotEmpty(objects)) {
            relateUrl(objects);
        }
        return body;
    }
    
    protected abstract Collection<?> destructReplaceObjets(Object body,
                                                           @NotNull MethodParameter returnType,
                                                           @NotNull MediaType selectedContentTy,
                                                           @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                                           @NotNull ServerHttpRequest request,
                                                           @NotNull ServerHttpResponse response);
    
    private void relateUrl(Collection<?> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return;
        }
        for (Object object : objects) {
            ReflectionUtils.doWithFields(object.getClass(), f -> {
                
                if (f.getType() != String.class) {
                    log.warn(
                            "Found a field annotated ReplaceURL annotation, but isn't String type. Filed info [ {}.{}]",
                            f.getDeclaringClass().getName(),
                            f.getName()
                    );
                    return;
                }
                
                // Check null
                f.setAccessible(true);
                Object urlValue = ReflectionUtils.getField(f, object);
                if (urlValue == null) {
                    return;
                }
                
                // replace
                String ossObjectName = urlValue.toString();
                String url = storageProvider.get().getURL(ossObjectName).toString();
                ReflectionUtils.setField(f, object, url);
                
            }, f -> f.isAnnotationPresent(ReplaceURL.class));
        }
    }
}