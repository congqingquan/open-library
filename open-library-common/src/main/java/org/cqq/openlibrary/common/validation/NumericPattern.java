package org.cqq.openlibrary.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Numeric pattern validation annotation
 *
 * @author Qingquan
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumericPatternValidator.class)
public @interface NumericPattern {
    
    // ============================================== JSR-303/JSR-380 constraint fields ==============================================
    
    String message();
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    // ================================================================================================================================
    
    String regexp();
}

