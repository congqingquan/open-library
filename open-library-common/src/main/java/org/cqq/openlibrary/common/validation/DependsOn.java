package org.cqq.openlibrary.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.openlibrary.common.domain.Pair;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Depends on validation annotation
 *
 * @author Qingquan
 */
@Repeatable(DependsOnList.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DependsOnValidator.class)
public @interface DependsOn {
    
    // ============================================== JSR-303/JSR-380 constraint fields ==============================================
    
    String message();
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    // ================================================================================================================================
    
    String triggerValue();
    
    String dependentField();
    
    CheckingMethod checkingMethod();
    
    String checkingRegex() default "";
    
    @Getter
    @AllArgsConstructor
    enum CheckingMethod {
        NOT_NULL(Objects::nonNull),
        NOT_BLANK(value -> value instanceof String str && !str.isBlank()),
        NOT_EMPTY(value -> {
            if (value instanceof String) {
                return !((String) value).isEmpty();
            } else if (value instanceof Iterable<?> iter) {
                return iter.iterator().hasNext();
            } else if (value instanceof Map<?, ?> map) {
                return !map.isEmpty();
            } else if (value instanceof Object[]) {
                return ((Object[]) value).length > 0;
            } else {
                return value != null;
            }
        }),
        @SuppressWarnings("unchecked")
        REGEX(pair -> {
            Pair<String, Object> regexAndValue = (Pair<String, Object>) pair;
            String regex = regexAndValue.getKey();
            Object value = regexAndValue.getValue();
            return value != null && value.toString().matches(regex);
        })
        ;
        
        private final Function<Object, Boolean> validFn;
    }
}