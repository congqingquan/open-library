package org.cqq.openlibrary.common.component.elementaccessor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

/**
 * Element accessor
 *
 * @author Qingquan
 */
@Deprecated
@Data
@AllArgsConstructor
public abstract class ElementAccessor {
    
    public abstract boolean support(Object object);
    
    public abstract void doAccess(Object object, Collection<ElementAccessor> accessors);
    
    protected boolean support(Object value, Collection<ElementAccessor> accessors) {
        for (ElementAccessor accessor : accessors) {
            if (accessor.support(value)) {
                return true;
            }
        }
        return false;
    }
    
    public static void access(Object object, Collection<ElementAccessor> accessors) {
        for (ElementAccessor accessor : accessors) {
            accessor.doAccess(object, accessors);
        }
    }
}
