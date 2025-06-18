package org.cqq.openlibrary.storage.handler.replaceurl.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cqq.openlibrary.storage.StorageProvider;

import java.lang.reflect.Method;

/**
 * Response replace url handler
 *
 * @author Qingquan
 */
@Data
@AllArgsConstructor
public abstract class ResponseReplaceURLHandler {
    
    // Get a storage instance dynamically
    private final StorageProvider storageProvider;
    
    public abstract Object replaceURL(Method method, Object returnValue);
}
