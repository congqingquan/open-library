package org.cqq.openlibrary.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Depends on list validation annotation
 *
 * @author Qingquan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DependsOnList {
    
    DependsOn[] value();
}