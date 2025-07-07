package org.cqq.openlibrary.common.util;

import org.cqq.openlibrary.common.exception.server.ReflectionRuntimeException;
import org.cqq.openlibrary.common.func.checked.CheckedSupplier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

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
    
    // ================================== generic ==================================
    
    public static Collection<Type> getGenericSuperclass(Object object) {
        Collection<Type> types = new ArrayList<>();
        Type genericSuperclass = object.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            types.addAll(Arrays.asList(parameterizedType.getActualTypeArguments()));
        }
        return types;
    }
    
    public static Optional<Type> getSingleGenericSuperclass(Object object) {
        Type genericSuperclass = object.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            return Optional.of(parameterizedType.getActualTypeArguments()[0]);
        }
        return Optional.empty();
    }
}