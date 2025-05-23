package org.cqq.openlibrary.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

/**
 * Enum name validator
 *
 * @author Qingquan
 */
public class EnumNameValidator implements ConstraintValidator<ValidEnumName, String> {
    
    private ValidEnumName constraintAnnotation;
    
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnumName constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> constraintAnnotation.ignoreCase() ? e.name().equalsIgnoreCase(value) : e.name().equals(value));
    }
}