package org.cqq.openlibrary.common.validation.validator;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.cqq.openlibrary.common.validation.annotation.ValidEnumName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enum name validator
 *
 * @author Qingquan
 */
public class EnumNameValidator implements ConstraintValidator<ValidEnumName, Object> {
    
    private ValidEnumName constraintAnnotation;
    
    private Set<Enum<?>> appliedEnumConstants;
    
    private Set<Enum<?>> excludedEnumConstants;
    
    @Override
    public void initialize(ValidEnumName constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        Set<String> excludedEnumConstantNames = Arrays.stream(constraintAnnotation.excludedEnumConstantNames()).collect(Collectors.toSet());
        
        this.appliedEnumConstants = Arrays.stream(enumConstants)
                .filter(e -> !excludedEnumConstantNames.contains(e.name()))
                .collect(Collectors.toSet());
        
        this.excludedEnumConstants = Arrays.stream(enumConstants)
                .filter(e -> excludedEnumConstantNames.contains(e.name()))
                .collect(Collectors.toSet());
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        
        // get string value
        Collection<String> stringValues = getStrings(value);
        
        // required check
        if (stringValues.isEmpty()) {
            return constraintAnnotation.skipIfNullOrEmpty();
        }
        
        // valid check
        boolean ignoreCase = constraintAnnotation.ignoreCase();
        for (String stringValue : stringValues) {
            boolean isValid = appliedEnumConstants.stream()
                    .anyMatch(e -> ignoreCase ? e.name().equalsIgnoreCase(stringValue) : e.name().equals(stringValue));
            if (!isValid) {
                return false;
            }
        }
        return true;
    }
    
    private Collection<String> getStrings(Object value) {
        Collection<String> stringValues = new ArrayList<>();
        if (value == null) {
            return stringValues;
        }
        
        // String
        if (value instanceof String stringValue) {
            String splitRegex = constraintAnnotation.splitRegex();
            // Single string value
            if (splitRegex.isEmpty()) {
                stringValues.add(stringValue);
            }
            // Can be split string value
            else {
                stringValues.addAll(Arrays.stream(stringValue.split(splitRegex)).toList());
            }
        }
        // Collection<String>
        else if (value instanceof Collection<?> values) {
            for (Object el : values) {
                if (el instanceof String stringValue) {
                    stringValues.add(stringValue);
                } else {
                    throw new ConstraintDeclarationException("@ValidEnumName: Element type of declaration collection field must be `String`");
                }
            }
        }
        // other type
        else {
            throw new ConstraintDeclarationException("@ValidEnumName: Declaration field type must be `String` or `Collection<String>`");
        }
        return stringValues;
    }
}