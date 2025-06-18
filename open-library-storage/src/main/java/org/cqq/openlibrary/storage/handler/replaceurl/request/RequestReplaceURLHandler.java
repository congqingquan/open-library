package org.cqq.openlibrary.storage.handler.replaceurl.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cqq.openlibrary.storage.StorageProvider;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Request replace url handler
 *
 * @author Qingquan
 */

@Data
@AllArgsConstructor
public abstract class RequestReplaceURLHandler {
    
    // Get a storage instance dynamically
    protected final StorageProvider storageProvider;
    
    public abstract void replaceURL(Method method, Parameter[] parameters, Object[] arguments);
}
