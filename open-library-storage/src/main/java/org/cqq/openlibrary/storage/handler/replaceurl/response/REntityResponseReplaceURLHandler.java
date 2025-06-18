package org.cqq.openlibrary.storage.handler.replaceurl.response;

import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.domain.R;
import org.cqq.openlibrary.common.util.RegexUtils;
import org.cqq.openlibrary.common.util.StringUtils;
import org.cqq.openlibrary.storage.StorageProvider;
import org.cqq.openlibrary.storage.handler.replaceurl.ReplaceURLSearchHelper;

import java.lang.reflect.Method;

/**
 * R entity response replace url handler
 *
 * @author Qingquan
 */
@Slf4j
public class REntityResponseReplaceURLHandler extends ResponseReplaceURLHandler {

    public REntityResponseReplaceURLHandler(StorageProvider storageProvider) {
        super(storageProvider);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object replaceURL(Method method, Object returnValue) {

        if (!(returnValue instanceof R)) {
            return returnValue;
        }

        R<Object> r = (R<Object>) returnValue;
        Object data = r.getData();
        if (data == null) {
            return null;
        }
        
        Object newData = ReplaceURLSearchHelper.replaceObjectFieldURL(data, (annotation, url) -> {
            if (StringUtils.isEmpty(url)) {
                return url;
            }
            switch (annotation.structure()) {
                case SIMPLE_URL -> {
                    return getStorageProvider().get().getURL(url).toString();
                }
                case URL_IN_IMG_TAG -> {
                    return RegexUtils.replaceSrc(
                            url,
                            (imgTag, imgSrc) -> {
                                if (StringUtils.isEmpty(imgSrc) ) {
                                    return imgTag;
                                }
                                return imgTag.replace(imgSrc, getStorageProvider().get().getURL(imgSrc).toString());
                            }
                    );
                }
                // Do nothing
                default -> {
                    return url;
                }
            }
        });
        
        r.setData(newData);
        return r;
    }
}
