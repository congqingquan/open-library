package org.cqq.openlibrary.common.util;

import org.cqq.openlibrary.common.exception.server.ReflectionRuntimeException;
import org.cqq.openlibrary.common.func.checked.CheckedSupplier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Reflection utils
 *
 * @author Qingquan
 */
public class ReflectionUtils {
    
    private ReflectionUtils() {
    }
    
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
    
    // ==========================================================================
    
    public static boolean isPrimitive(Object object) {
        return isPrimitive(object.getClass());
    }
    
    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Character.class);
    }
    
    // ================================== Field ==================================
    
    public static Object getFieldValue(Object object, Field field) {
        return catchReflectionException(() -> field.get(object));
    }
    
    private static <R> R catchReflectionException(CheckedSupplier<R, Exception> runnable) {
        try {
            return runnable.get();
        } catch (Exception e) {
            throw new ReflectionRuntimeException(e);
        }
    }
}