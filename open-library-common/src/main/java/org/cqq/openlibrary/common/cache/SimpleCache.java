package org.cqq.openlibrary.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple cache
 *
 * @author Qingquan
 */
public class SimpleCache {
    
    private static final Map<Object, Object> container = new ConcurrentHashMap<>();
    
    public static void put(Object key, Object value) {
        container.put(key, value);
    }
    
    @SuppressWarnings({"unchecked"})
    public static <V> V get(Object key) {
        return (V) container.get(key);
    }
}