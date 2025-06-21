package org.cqq.openlibrary.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valid enum name validation annotation
 *
 * @author Qingquan
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumNameValidator.class)
public @interface ValidEnumName {
    
    // ============================================== JSR-303/JSR-380 constraint fields ==============================================
    
    String message() default "Invalid enum name";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    // ================================================================================================================================
    
    Class<? extends Enum<?>> enumClass();
    
    String[] excludedEnumConstantNames() default {};
    
    boolean ignoreCase() default false;
    
    String splitRegex() default "";
    
    boolean skipIfNullOrEmpty() default true;
}