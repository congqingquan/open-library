package org.cqq.openlibrary.common.component.elementaccessor;

import lombok.AllArgsConstructor;
import org.cqq.openlibrary.common.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Function;

/**
 * Collection element accessor
 *
 * @author Qingquan
 */
@Deprecated
@AllArgsConstructor
public class CollectionElementAccessor extends ElementAccessor {
    
    private final Function<Collection<Object>, Boolean> postProcessor;
    
    @Override
    public boolean support(Object object) {
        return object instanceof Collection<?>;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void doAccess(Object object, Collection<ElementAccessor> accessors) {
        if (!(object instanceof Collection<?> collection)) {
            return;
        }
        if (postProcessor.apply((Collection<Object>) collection)) {
            return;
        }
        for (Object data : collection) {
            //  Skip primitive type or string type data
            if (ReflectionUtils.isPrimitive(data) || data instanceof String) {
                return;
            }
            // Nested collection data
            else if (data instanceof Collection) {
                for (ElementAccessor accessor : accessors) {
                    if (accessor.support(data)) {
                        accessor.doAccess(data, accessors);
                    }
                }
            }
            // Other data
            else {
                Field[] declaredFields = data.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    Object fieldValue = ReflectionUtils.getFieldValue(data, declaredField);
                    for (ElementAccessor accessor : accessors) {
                        if (accessor.support(fieldValue)) {
                            accessor.doAccess(fieldValue, accessors);
                        }
                    }
                }
            }
        }
    }
}
