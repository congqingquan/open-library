package org.cqq.openlibrary.common.util;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Object utils
 *
 * @author Qingquan
 */
public class ObjectUtils {
    
    private ObjectUtils() {}
    
    public static <T> T defaultIfNull(T obj, T defaultValue) {
        return obj == null ? defaultValue : obj;
    }
    
    /**
     * 处理空值
     * @param obj1         对象一
     * @param obj2         对象二
     * @param eitherIsNull 当 obj1 或 obj2 为空时，执行该函数，并传递非空的对象
     * @param func         处理参数对象的函数
     */
    public static <T, R> R handleNull(T obj1, T obj2, Function<T, R> eitherIsNull, BiFunction<T, T, R> func) {
        if (obj1 == null && obj2 == null) {
            return null;
        }
        if (obj1 == null || obj2 == null) {
            return eitherIsNull.apply(obj1 == null ? obj2 : obj1);
        }
        return func.apply(obj1, obj2);
    }
    
    public static <T> Boolean compareTo(Comparable<T> obj1, T obj2, BiPredicate<Comparable<T>, T> predicate) {
        return (obj1 == obj2) || (obj1 != null && predicate.test(obj1, obj2));
    }
}
