package org.cqq.openlibrary.common.types.union;

/**
 * Union two type
 *
 * @author Qingquan
 */
public class Union2Type<T1, T2> extends UnionType {
    
    public Union2Type(Object object) {
        super(object);
    }
    
    public Boolean isT1() {
        return super.isTN(1);
    }
    
    public Boolean isT2() {
        return super.isTN(2);
    }
    
    @SuppressWarnings("unchecked")
    public T1 toT1() {
        isTNCheck(1);
        return (T1) super.object;
    }
    
    @SuppressWarnings("unchecked")
    public T2 toT2() {
        isTNCheck(2);
        return (T2) super.object;
    }
}
