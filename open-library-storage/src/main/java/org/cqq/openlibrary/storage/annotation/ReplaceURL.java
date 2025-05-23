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
        ElementType.FIELD
})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ReplaceURL {
}
