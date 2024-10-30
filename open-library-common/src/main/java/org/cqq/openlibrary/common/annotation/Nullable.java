package org.cqq.openlibrary.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Nullable annotation
 *
 * @author Qingquan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE_PARAMETER,
        ElementType.TYPE_USE,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.CONSTRUCTOR,
        ElementType.METHOD,
})
public @interface Nullable {

}