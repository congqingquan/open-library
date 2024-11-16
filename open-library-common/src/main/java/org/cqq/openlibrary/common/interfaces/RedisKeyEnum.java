package org.cqq.openlibrary.common.interfaces;

import org.cqq.openlibrary.common.util.CollectionUtils;

/**
 * Redis key enum
 *
 * @author Qingquan
 */
public interface RedisKeyEnum {
    
    String key(String... values);
    
    static String key(Enum<?> e, String... values) {
        return key(e, ":", values);
    }
    
    static String key(Enum<?> e, String delimiter, String... values) {
        return String.join(
                delimiter,
                CollectionUtils.addAll(
                        CollectionUtils.newArrayList(e.name()), values
                )
        );
    }
}
