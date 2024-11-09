package org.cqq.openlibrary.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Returnable bean utils
 *
 * @author Qingquan
 */
public class ReturnableBeanUtils {

    private ReturnableBeanUtils() {
    }

    public static <S, T> T copyProperties(S source, T target) {
        if (source == null) {
            return null;
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <S, T> T copyProperties(S source, Supplier<? extends T> targetSupplier) {
        if (source == null) {
            return null;
        }
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


    public static <S, T> Page<T> copyPage(Page<S> page, Page<T> targetPage) {
        ArrayList<T> records = new ArrayList<>();
        for (S record : page.getRecords()) {
            T item = (T) new Object();
            records.add(copyProperties(record, item));
        }
        return copyProperties(page, targetPage);
    }
}