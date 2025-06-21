package org.cqq.openlibrary.spring.autoconfigure.condition;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * On properties condition
 *
 * @author Qingquan
 */
public class OnPropertiesCondition implements Condition {
    
    @Override
    public boolean matches(@NotNull ConditionContext context, AnnotatedTypeMetadata metadata) {
        String annotationName = ConditionalOnProperties.class.getName();
        if (!metadata.isAnnotated(annotationName)) {
            return false;
        }
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(annotationName);
        if (annotationAttributes == null) {
            return false;
        }
        Environment environment = context.getEnvironment();
        Boolean allMatch = (Boolean) annotationAttributes.get("allMatch");
        AnnotationAttributes[] properties = (AnnotationAttributes[])annotationAttributes.get("properties");
        int matchCount = 0;
        for (AnnotationAttributes property : properties) {
            String name = property.getString("name");
            String valueRegex = property.getString("havingValueRegex");
            String value = environment.getProperty(name);
            if (value == null || !Pattern.matches(valueRegex, value)) {
                // return early
                if (allMatch) {
                    return false;
                }
                continue;
            }
            matchCount++;
        }
        return allMatch ? matchCount == properties.length : matchCount > 0;
    }
}