package org.cqq.openlibrary.common.util;

import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Returnable bean utils
 *
 * @author Qingquan.Cong
 */
public class ReturnableBeanUtils {

    private ReturnableBeanUtils() {}

    public static <S, T> T copyProperties(S source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }
    
    public static <S, T> T copyProperties(S source, Supplier<? extends T> targetSupplier) {
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }
    
    public static <T, C extends Collection<T>> C copyCollection(Collection<?> sourceColl,
                                                                Supplier<? extends T> targetSupplier,
                                                                Supplier<C> container) {
        return sourceColl
                .stream()
                .map(source -> copyProperties(source, targetSupplier))
                .collect(Collectors.toCollection(container));
    }
}