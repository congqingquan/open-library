package org.cqq.openlibrary.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Map builder
 *
 * @author Qingquan.Cong
 */
public class MapBuilder<K, V> {
    
    private final Map<K, V> container;
    
    private MapBuilder(Map<K, V> container) {
        this.container = container;
    }
    
    public MapBuilder<K, V> put(K k, V v) {
        return put(true, k, v);
    }

    public MapBuilder<K, V> put(boolean condition, K k, V v) {
        if (condition) {
            container.put(k, v);
        }
        return this;
    }

    public MapBuilder<K, V> putAll(Map<K, V> map) {
        return putAll(true, map);
    }

    public MapBuilder<K, V> putAll(boolean condition, Map<K, V> map) {
        if (condition) {
            container.putAll(map);
        }
        return this;
    }

    public Map<K, V> build() {
        return container;
    }

    public static <K, V> MapBuilder<K, V> builder() {
        return builder(new HashMap<>());
    }
    
    public static <K, V> MapBuilder<K, V> builder(Map<K, V> container) {
        return new MapBuilder<>(container);
    }
}