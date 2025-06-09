package org.cqq.openlibrary.common.util;

/**
 * Assert
 *
 * @author Qingquan
 */
public class Assert {
    
    private Assert() {}
    
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
    
    public void notEmpty(Object object, String message) {
        if (ObjectUtils.areEmpty(object)) {
            throw new IllegalArgumentException(message);
        }
    }
    
    // ================================== String ==================================
    
    public static void hasLength(String str, String message) {
        if (ObjectUtils.areEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }
    
    public static void hasLength(String str, int length, String message) {
        if (str == null || str.length() != length) {
            throw new IllegalArgumentException(message);
        }
    }
    
    public static void hasLength(String str, int min, int max, String message) {
        if (str == null || str.length() < min || str.length() > max) {
            throw new IllegalArgumentException(message);
        }
    }
    
    public static void notBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(message);
        }
    }
}
