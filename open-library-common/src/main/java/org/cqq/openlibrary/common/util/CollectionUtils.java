package org.cqq.openlibrary.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Collection utils
 *
 * @author Qingquan.Cong
 */
public class CollectionUtils {
    
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }
    
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    public static <T> ArrayList<T> newArrayList(T... elements) {
        return newCollection(() -> new ArrayList<>(elements.length), elements);
    }
    
    public static <T, C extends Collection<T>> C newCollection(Supplier<C> container, T... elements) {
        C coll = container.get();
        coll.addAll(Arrays.asList(elements));
        return coll;
    }
}
