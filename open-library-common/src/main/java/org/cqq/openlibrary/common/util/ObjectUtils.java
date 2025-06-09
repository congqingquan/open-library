package org.cqq.openlibrary.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Object utils
 *
 * @author Qingquan
 */
public class ObjectUtils {
    
    private ObjectUtils() {
    }
    
    // Optional.ofNullable(value).orElse(defaultValue)
    public static <T> T defaultIfNull(T obj, T defaultValue) {
        return obj == null ? defaultValue : obj;
    }
    
    // Optional.ofNullable(value).ifPresent(valueConsumer);
    public static <T> void consumeIfNotNull(T obj, Consumer<T> consumer) {
        if (obj == null) {
            return;
        }
        consumer.accept(obj);
    }
    
    public static boolean areNull(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areNotNull(Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areEmpty(Object... objects) {
        for (Object obj : objects) {
            if (obj instanceof Optional<?> optional) {
                if (optional.isPresent()) {
                    return false;
                }
            } else if (obj instanceof CharSequence charSequence) {
                if (!charSequence.isEmpty()) {
                    return false;
                }
            } else if (obj != null && obj.getClass().isArray()) {
                if (Array.getLength(obj) > 0) {
                    return false;
                }
            } else if (obj instanceof Iterable<?> iterable) {
                if (iterable.iterator().hasNext()) {
                    return false;
                }
            } else if (obj instanceof Map<?, ?> map) {
                if (!map.isEmpty()) {
                    return false;
                }
            } else if (obj != null) {
                return false;
            }
        }
        // else
        return true;
    }
    
    public static boolean areNotEmpty(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return false;
            } else if (obj instanceof Optional<?> optional && optional.isEmpty()) {
                return false;
            } else if (obj instanceof CharSequence charSequence && charSequence.isEmpty()) {
                return false;
            } else if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
                return false;
            } else if (obj instanceof Collection<?> collection && collection.isEmpty()) {
                return false;
            } else if (obj instanceof Map<?, ?> map && map.isEmpty()) {
                return false;
            }
        }
        // else
        return true;
    }
    
    /**
     * 处理空值
     *
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
