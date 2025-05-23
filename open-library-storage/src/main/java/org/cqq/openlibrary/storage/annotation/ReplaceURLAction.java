package org.cqq.openlibrary.storage.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Replace URL action
 *
 * @author Qingquan
 */
@Documented
@Target({
        java.lang.annotation.ElementType.METHOD
})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ReplaceURLAction {
    

}
