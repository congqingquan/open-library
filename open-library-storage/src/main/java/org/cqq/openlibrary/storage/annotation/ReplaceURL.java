package org.cqq.openlibrary.storage.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Replace URL
 *
 * @author Qingquan
 */
@Documented
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.METHOD
})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ReplaceURL {
    
    Structure structure() default Structure.SIMPLE_URL;
    
    enum Structure {
        SIMPLE_URL,
        URL_IN_IMG_TAG,
        ;
    }
}
