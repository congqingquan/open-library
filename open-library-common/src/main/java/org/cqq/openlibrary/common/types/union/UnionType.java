package org.cqq.openlibrary.common.types.union;

import org.cqq.openlibrary.common.util.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Union type
 *
 * @author Qingquan
 */
public abstract class UnionType {
    
    protected final List<Type> types;
    
    protected final Object object;
    
    public UnionType(Object object) {
        this.types = new ArrayList<>(ReflectionUtils.getGenericSuperclass(this));
        this.object = object;
        if (!isValidType(object)) {
            throw new IllegalArgumentException("UnionType: " + object.getClass().getName() + " is not a valid type");
        }
    }
    
    protected Boolean isValidType(Object object) {
        String valueClassName = object.getClass().getName();
        for (Type type : types) {
            if (type.getTypeName().equals(valueClassName)) {
                return true;
            }
        }
        return false;
    }
    
    protected Type getTN(Integer n) {
        return types.get(n - 1);
    }
    
    protected Boolean isTN(Integer n) {
        return object.getClass().getName().equals(getTN(n).getTypeName());
    }
    
    protected void isTNCheck(Integer n) {
        if (!isTN(n)) {
            throw new IllegalArgumentException("UnionType: " + object.getClass().getName() + " is not a valid type for " + getTN(n));
        }
    }
}
