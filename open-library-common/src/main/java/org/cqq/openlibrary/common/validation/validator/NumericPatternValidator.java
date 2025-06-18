package org.cqq.openlibrary.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.cqq.openlibrary.common.validation.annotation.NumericPattern;

import java.util.regex.Pattern;

/**
 * Numeric pattern validator
 *
 * @author Qingquan
 */
public class NumericPatternValidator
       implements ConstraintValidator<NumericPattern, Integer> {
    
    private NumericPattern constraintAnnotation;
    
    private Pattern pattern;
    
    @Override
    public void initialize(NumericPattern constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        this.pattern = Pattern.compile(constraintAnnotation.regexp());
    }
    
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && pattern.matcher(value.toString()).matches();
    }
}