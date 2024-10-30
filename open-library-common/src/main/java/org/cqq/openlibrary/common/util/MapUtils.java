package org.cqq.openlibrary.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * Map utils
 *
 * @author Qingquan
 */
public class MapUtils {
    
    // ====================================== Create ======================================
    
    private static final int MAX_POWER_OF_TWO = 1 << 30;
    
    // Copy from guava
    public static int capacity(int expectedSize) {
        if (expectedSize < 0) {
            throw new IllegalArgumentException("Expected size cannot less than zero");
        }
        if (expectedSize < 3) {
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            // This is the calculation used in JDK8 to resize when a putAll
            // happens; it seems to be the most conservative calculation we
            // can make.  0.75 is the default load factor.
            return (int) ((float) expectedSize / 0.75F + 1.0F);
        }
        return Integer.MAX_VALUE; // any large value
    }
    
    public static <K, V> HashMap<K, V> newHashMap(int expectedSize) {
        return new HashMap<>(capacity(expectedSize));
    }
    
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int expectedSize) {
        return new LinkedHashMap<>(capacity(expectedSize));
    }
    
    public static <K, V> TreeMap<K, V> newTreeMap(Comparator<? super K> comparator) {
        return new TreeMap<>(comparator);
    }
    
    // ====================================== Create - Builder ======================================
    
    public static class Builder<K, V> {
        
        private final Map<K, V> container;
        
        private Builder(Map<K, V> container) {
            this.container = container;
        }
        
        public Builder<K, V> put(K k, V v) {
            return put(true, k, v);
        }
        
        public Builder<K, V> put(boolean condition, K k, V v) {
            if (condition) {
                container.put(k, v);
            }
            return this;
        }
        
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            return putAll(true, map);
        }
        
        public Builder<K, V> putAll(boolean condition, Map<? extends K, ? extends V> map) {
            if (condition) {
                container.putAll(map);
            }
            return this;
        }
        
        public Map<K, V> build() {
            return container;
        }
        
        public static <K, V> Builder<K, V> builder() {
            return builder(new HashMap<>());
        }
        
        public static <K, V> Builder<K, V> builder(Map<K, V> container) {
            return new Builder<>(container);
        }
    }
    
    // ====================================== Search ======================================
    
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    
    // For LinkedHashMap
    public static <V> Optional<V> getFirstKey(Map<?, ? extends V> map) {
        Iterator<? extends V> iterator = map.values().iterator();
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }
    
    @SafeVarargs
    public static <K, V> ArrayList<? extends V> getKeys(Map<K, ? extends V> map, K... keys) {
        return getKeys(map, ArrayList::new, false, keys);
    }
    
    @SafeVarargs
    public static <K, V, C extends Collection<? super V>> C getKeys(Map<K, ? extends V> map,
                                                                    Supplier<? extends C> container,
                                                                    Boolean filterNull,
                                                                    K... keys) {
        C coll = container.get();
        for (K key : keys) {
            V value = map.get(key);
            if (filterNull && value == null) {
                continue;
            }
            coll.add(value);
        }
        return coll;
    }
}