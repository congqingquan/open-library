package org.cqq.openlibrary.storage.handler.replaceurl.request;

import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.exception.server.ServerException;
import org.cqq.openlibrary.common.util.RegexUtils;
import org.cqq.openlibrary.common.util.StringUtils;
import org.cqq.openlibrary.storage.StorageProvider;
import org.cqq.openlibrary.storage.annotation.ReplaceURL;
import org.cqq.openlibrary.storage.handler.replaceurl.ReplaceURLSearchHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Default request replace url handler
 *
 * @author Qingquan
 */
@Slf4j
public class DefaultRequestReplaceURLHandler extends RequestReplaceURLHandler {
    
    public DefaultRequestReplaceURLHandler(StorageProvider storageProvider) {
        super(storageProvider);
    }
    
    @Override
    public void replaceURL(Method method, Parameter[] parameters, Object[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            if (!parameters[i].isAnnotationPresent(ReplaceURL.class)) {
                continue;
            }
            arguments[i] = ReplaceURLSearchHelper.replaceObjectFieldURL(
                    arguments[i],
                    (annotation, url) -> {
                        if (StringUtils.isEmpty(url) ) {
                            return url;
                        }
                        ReplaceURL.Structure structure = annotation.structure();
                        switch (structure) {
                            case SIMPLE_URL -> {
                                String domain = StringUtils.appendSuffix(storageProvider.get().getDomain(), "/", false);
                                if (!url.startsWith(domain)) {
                                    throw new ServerException("Storage domain has been changed detected, retry fetching data.");
                                }
                                return url.replace(domain, "");
                            }
                            case URL_IN_IMG_TAG -> {
                                return RegexUtils.replaceSrc(
                                        url,
                                        (imgTag, imgSrc) -> {
                                            if (StringUtils.isEmpty(imgSrc) ) {
                                                return imgTag;
                                            }
                                            String domain = StringUtils.appendSuffix(storageProvider.get().getDomain(), "/", false);
                                            if (!imgSrc.startsWith(domain)) {
                                                throw new ServerException("Storage domain has been changed detected, retry fetching data.");
                                            }
                                            return imgTag.replace(
                                                    imgSrc,
                                                    imgSrc.replace(domain, "")
                                            );
                                        }
                                );
                            }
                            // Do nothing
                            default -> {
                                return url;
                            }
                        }
                    }
            );
        }
    }
}
