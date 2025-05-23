package org.cqq.openlibrary.storage.controlleradvice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.cqq.openlibrary.common.domain.R;
import org.cqq.openlibrary.storage.StorageProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Default replace url response handler
 *
 * @author Qingquan
 */
public class DefaultReplaceURLResponseHandler extends ReplaceURLResponseHandler {
    
    public DefaultReplaceURLResponseHandler(StorageProvider provider) {
        super(provider);
    }
    
    @Override
    protected Collection<?> destructReplaceObjets(Object body,
                                                  @NotNull MethodParameter returnType,
                                                  @NotNull MediaType selectedContentTy,
                                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                                  @NotNull ServerHttpRequest request,
                                                  @NotNull ServerHttpResponse response) {
        if (!(body instanceof R<?>)) {
            return Collections.emptyList();
        }
        Object data = ((R<?>) body).getData();
        // R { code: n, data: null, message: "" }
        if (data == null) {
            return Collections.emptyList();
        }
        
        // R { code: n, data: [], message: "" }
        if (data instanceof Collection<?> datas) {
            return datas;
        }
        // R { code: n, data: { current: 1, pageSize: 10, record: [] }, message: "" }
        else if (data instanceof IPage<?> pages) {
            return pages.getRecords();
        }
        // R { code: n, data: {}, message: "" }
        else {
            return List.of(List.of(data));
        }
    }
}