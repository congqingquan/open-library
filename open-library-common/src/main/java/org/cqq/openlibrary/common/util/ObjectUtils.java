package org.cqq.openlibrary.common.util;

/**
 * Object utils
 *
 * @author CongQingquan
 */
public class ObjectUtils {
    
    private ObjectUtils() {}
    
    public static <T> T defaultIfNull(T obj, T defaultValue) {
        return obj == null ? defaultValue : obj;
    }
}
