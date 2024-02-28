package org.cqq.openlibrary.common.util;

/**
 * Reflection utils
 *
 * @author Qingquan.Cong
 */
public class ReflectionUtils {

    public static <T> T newInstance(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz cannot be null");
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return ExceptionUtils.sneakyThrow(e);
        }
    }
}