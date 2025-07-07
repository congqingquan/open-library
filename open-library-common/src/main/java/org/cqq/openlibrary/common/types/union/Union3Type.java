package org.cqq.openlibrary.common.types.union;

/**
 * Union three type
 *
 * @author Qingquan
 */
public class Union3Type<T1, T2, T3> extends Union2Type<T1, T2> {
    
    public Union3Type(Object object) {
        super(object);
    }
    
    public Boolean isT3() {
        return super.isTN(3);
    }
    
    @SuppressWarnings("unchecked")
    public T3 toT3() {
        isTNCheck(3);
        return (T3) super.object;
    }
}
