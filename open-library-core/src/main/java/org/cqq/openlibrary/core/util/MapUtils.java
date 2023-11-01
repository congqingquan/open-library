package org.cqq.openlibrary.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Map utils
 *
 * @author Qingquan.Cong
 */
public class MapUtils {
    
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
}