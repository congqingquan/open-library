package org.cqq.openlibrary.common.validation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.cqq.openlibrary.common.util.HttpContext;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.common.util.StringUtils;

/**
 * Depends on validator (Read dependent field value from request body)
 *
 * @author Qingquan
 */
public class DependsOnValidator implements ConstraintValidator<DependsOn, Object> {
    
    private DependsOn constraintAnnotation;

    @Override
    public void initialize(DependsOn constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        
        if (!constraintAnnotation.triggerValue().equals(value)) {
            return true;
        }
        
        String dependentField = constraintAnnotation.dependentField();
        if (StringUtils.isBlank(dependentField)) {
            return false;
        }
        
        // Search dependent field value
        HttpServletRequest request = HttpContext.getRequest();
        Object dependentFieldValue = request.getParameter(dependentField);
        if (dependentFieldValue == null) {
            // Try to get field value from application-json body
            String requestBody = HttpContext.getRequestBody(request);
            if (JSONUtils.isJSON(requestBody)) {
                dependentFieldValue = JSONUtils.parse2Map(requestBody).get(dependentField);
            }
        }
        
        // Check
        return constraintAnnotation.checkingMethod().getValidFn().apply(dependentFieldValue);
    }
}