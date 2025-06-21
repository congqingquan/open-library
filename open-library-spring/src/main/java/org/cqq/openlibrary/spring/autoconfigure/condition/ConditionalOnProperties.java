package org.cqq.openlibrary.spring.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Conditional on properties
 *
 * @author Qingquan
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnPropertiesCondition.class)
public @interface ConditionalOnProperties {
    
    Property[] properties() default {};
    
    boolean allMatch() default true;
    
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Property {
        
        String name();
        
        String havingValueRegex() default ".*";
    }
}