package org.cqq.openlibrary.common.util;

import java.lang.reflect.InvocationTargetException;

/**
 * Reflection utils
 *
 * @author Qingquan
 */
public class ReflectionUtils {

    public static <T> T newInstance(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz cannot be null");
        }
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return ExceptionUtils.sneakyThrow(e);
        }
    }
}