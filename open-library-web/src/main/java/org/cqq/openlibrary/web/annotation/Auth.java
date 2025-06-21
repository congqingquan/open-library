package org.cqq.openlibrary.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鉴权注解
 *
 * @author Qingquan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
public @interface Auth {
    
    /**
     * 登录鉴权
     */
    boolean authenticate() default true;
    
    /**
     * 权限鉴权
     */
    boolean authorize() default true;
}
